package org.jmqtt.broker;

import org.jmqtt.broker.BrokerController;
import org.jmqtt.broker.dispatcher.InnerMessageTransfer;
import org.jmqtt.broker.dispatcher.MessageDispatcher;
import org.jmqtt.common.bean.Message;
import org.jmqtt.common.bean.MessageHeader;
import org.jmqtt.remoting.util.MessageUtil;
import org.jmqtt.store.RetainMessageStore;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex Liu
 * @date 2019/11/13
 */
public class ProduceForYunba {
    private static MessageDispatcher messageDispatcher;
    private static InnerMessageTransfer messageTransfer;
    private static RetainMessageStore retainMessageStore;

    private static final String broker = "tcp://127.0.0.1:1883";
    private static final String content = "{\"tranId\":\"3BB5666C81C747BFAAC558651D4954C\",\"commandId\":\"LOOKUP\",\"commandContent\":{}}";
    private static final int qos = 1;
    private static final String topic = "T00000493012";
    private static final String clientId = "PRODUCER_FOR_YUNBA";

    public static void go(BrokerController brokerController){
        System.out.println("go");

        if(brokerController == null){
            System.out.println("null");
        }
        messageDispatcher = brokerController.getMessageDispatcher();
        retainMessageStore = brokerController.getRetainMessageStore();

        Message publishMessage = new Message();
        publishMessage.setMsgId(MessageUtil.generateMessageId());
        publishMessage.setClientId(clientId);
        publishMessage.setType(Message.Type.PUBLISH);
        publishMessage.setPayload(content.getBytes());

        Map<String,Object> headers = new HashMap<>();
        headers.put(MessageHeader.TOPIC,topic);
        headers.put(MessageHeader.QOS,qos);
        headers.put(MessageHeader.RETAIN,false);
        headers.put(MessageHeader.DUP,false);
        publishMessage.setHeaders(headers);
        processMessage(publishMessage);
    }

    protected static void processMessage(Message message){
        messageDispatcher.appendMessage(message);
        boolean retain = (boolean) message.getHeader(MessageHeader.RETAIN);
        if(retain){
            int qos = (int) message.getHeader(MessageHeader.QOS);
            byte[] payload = message.getPayload();
            String topic = (String) message.getHeader(MessageHeader.TOPIC);
            //qos == 0 or payload is none,then clear previous retain message
            if(qos == 0 || payload == null || payload.length == 0){
                retainMessageStore.removeRetainMessage(topic);
            }else{
                retainMessageStore.storeRetainMessage(topic,message);
            }
        }
        //暂时是单机版
        //dispatcherMessage2Cluster(message);
    }
}
