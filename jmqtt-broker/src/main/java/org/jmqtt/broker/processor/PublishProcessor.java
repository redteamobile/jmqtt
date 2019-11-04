package org.jmqtt.broker.processor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.ReferenceCountUtil;
import org.jmqtt.broker.BrokerController;
import org.jmqtt.broker.acl.PubSubPermission;
import org.jmqtt.broker.subscribe.SubscriptionMatcher;
import org.jmqtt.common.bean.Subscription;
import org.jmqtt.common.bean.Topic;
import org.jmqtt.store.FlowMessageStore;
import org.jmqtt.remoting.session.ClientSession;
import org.jmqtt.common.bean.Message;
import org.jmqtt.common.bean.MessageHeader;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.remoting.netty.RequestProcessor;
import org.jmqtt.remoting.session.ConnectManager;
import org.jmqtt.remoting.util.MessageUtil;
import org.jmqtt.remoting.util.NettyUtil;
import org.jmqtt.store.RetainMessageStore;
import org.jmqtt.store.SubscriptionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PublishProcessor extends AbstractMessageProcessor implements RequestProcessor {
    private Logger log = LoggerFactory.getLogger(LoggerName.MESSAGE_TRACE);

    private FlowMessageStore flowMessageStore;

    private PubSubPermission pubSubPermission;

    private SubscriptionMatcher subscriptionMatcher;
    private RetainMessageStore retainMessageStore;
    private SubscriptionStore subscriptionStore;

    private static List<Integer> defaultQos = new ArrayList<>();

    static {
        Integer qos = new Integer(1);
        defaultQos.add(qos);
    }

    public PublishProcessor(BrokerController controller){
        super(controller.getMessageDispatcher(),controller.getRetainMessageStore(),controller.getInnerMessageTransfer());
        this.flowMessageStore = controller.getFlowMessageStore();
        this.pubSubPermission = controller.getPubSubPermission();
        this.subscriptionMatcher = controller.getSubscriptionMatcher();
        this.retainMessageStore = controller.getRetainMessageStore();
        this.subscriptionStore = controller.getSubscriptionStore();
    }

    @Override
    public void processRequest(ChannelHandlerContext ctx, MqttMessage mqttMessage) {

        log.info("---------------processRequest---------------");

        try{
            MqttPublishMessage publishMessage = (MqttPublishMessage) mqttMessage;
            MqttQoS qos = publishMessage.fixedHeader().qosLevel();
            Message innerMsg = new Message();
            String clientId = NettyUtil.getClientId(ctx.channel());
            ClientSession clientSession = ConnectManager.getInstance().getClient(clientId);
            String topic = publishMessage.variableHeader().topicName();
            if(!this.pubSubPermission.publishVerfy(clientId,topic)){
                log.warn("[PubMessage] permission is not allowed");
                clientSession.getCtx().close();
                return;
            }

            /*
            实现云巴的订阅模式：终端向",yali"这个topic发送publish消息，payload中带的是实际需要订阅的topic。
            这里监听到之后后台来实现订阅操作
             */
            if(",yali".equals(topic)){
                log.info("---------" + mqttMessage + "-------------");
                String messageId = publishMessage.variableHeader().messageId();
                MqttMessage subAckMessage = MessageUtil.getSubAckMessage(messageId , defaultQos);
                ctx.writeAndFlush(subAckMessage);
                //subscribe topic
                byte[] messagePayloadBytes = MessageUtil.readBytesFromByteBuf(((MqttPublishMessage) mqttMessage).payload());
                log.info("length = {}" , messagePayloadBytes.length);
                log.info(new String(messagePayloadBytes));
                byte[] topicNameBytes = new byte[messagePayloadBytes.length - 6];
                System.arraycopy(messagePayloadBytes , 6 , topicNameBytes ,0 ,  topicNameBytes.length);
                String topicName = new String(topicNameBytes);
                log.debug("------topic name = {} -----" , topicName);
                Topic subscribeTopic = new Topic(topicName , 1);
                subscribe(clientSession,subscribeTopic);
                return;
            }

            innerMsg.setPayload(MessageUtil.readBytesFromByteBuf(((MqttPublishMessage) mqttMessage).payload()));
            innerMsg.setClientId(clientId);
            innerMsg.setType(Message.Type.valueOf(mqttMessage.fixedHeader().messageType().value()));
            Map<String,Object> headers = new HashMap<>();
            headers.put(MessageHeader.TOPIC,publishMessage.variableHeader().topicName());
            headers.put(MessageHeader.QOS,publishMessage.fixedHeader().qosLevel().value());
            headers.put(MessageHeader.RETAIN,publishMessage.fixedHeader().isRetain());
            headers.put(MessageHeader.DUP,publishMessage.fixedHeader().isDup());
            innerMsg.setHeaders(headers);
            innerMsg.setMsgId(publishMessage.variableHeader().packetId());
            switch (qos){
                case AT_MOST_ONCE:
                    processMessage(innerMsg);
                    break;
                case AT_LEAST_ONCE:
                    processQos1(ctx,innerMsg);
                    break;
                case EXACTLY_ONCE:
                    processQos2(ctx,innerMsg);
                    break;
                default:
                    log.warn("[PubMessage] -> Wrong mqtt message,clientId={}", clientId);
            }
        }catch (Throwable tr){
            log.warn("[PubMessage] -> Solve mqtt pub message exception:{}",tr);
        }finally {
            ReferenceCountUtil.release(mqttMessage.payload());
        }
    }

    private void processQos2(ChannelHandlerContext ctx,Message innerMsg){
        log.debug("[PubMessage] -> Process qos2 message,clientId={}",innerMsg.getClientId());
        boolean flag = flowMessageStore.cacheRecMsg(innerMsg.getClientId(),innerMsg);
        if(!flag){
            log.warn("[PubMessage] -> cache qos2 pub message failure,clientId={}",innerMsg.getClientId());
        }
        MqttMessage pubRecMessage = MessageUtil.getPubRecMessage(innerMsg.getMsgId());
        ctx.writeAndFlush(pubRecMessage);
    }

    private void processQos1(ChannelHandlerContext ctx,Message innerMsg){
        processMessage(innerMsg);
        log.debug("[PubMessage] -> Process qos1 message,clientId={}",innerMsg.getClientId());
        MqttPubAckMessage pubAckMessage = MessageUtil.getPubAckMessage(innerMsg.getMsgId());
        ctx.writeAndFlush(pubAckMessage);
    }

    //just for yunba
    private List<Message> subscribe(ClientSession clientSession,Topic topic){
        Collection<Message> retainMessages = null;
        List<Message> needDispatcher = new ArrayList<>();
        Subscription subscription = new Subscription(clientSession.getClientId(),topic.getTopicName(),topic.getQos());
        log.info("---------clint {} subscribe topic to {}" , clientSession.getClientId() , topic.getTopicName());
        boolean subRs = this.subscriptionMatcher.subscribe(subscription);
        if(subRs){
            if(retainMessages == null){
                retainMessages = retainMessageStore.getAllRetainMessage();
            }
            for(Message retainMsg : retainMessages){
                String pubTopic = (String) retainMsg.getHeader(MessageHeader.TOPIC);
                if(subscriptionMatcher.isMatch(pubTopic,subscription.getTopic())){
                    int minQos = MessageUtil.getMinQos((int)retainMsg.getHeader(MessageHeader.QOS),topic.getQos());
                    retainMsg.putHeader(MessageHeader.QOS,minQos);
                    needDispatcher.add(retainMsg);
                }
            }
            this.subscriptionStore.storeSubscription(clientSession.getClientId(),subscription);
        }
        retainMessages = null;
        return needDispatcher;
    }

}
