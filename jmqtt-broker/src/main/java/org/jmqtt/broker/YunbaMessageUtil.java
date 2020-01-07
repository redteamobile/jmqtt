/*
package org.jmqtt.broker;

import org.jmqtt.broker.dispatcher.InnerMessageTransfer;
import org.jmqtt.broker.dispatcher.MessageDispatcher;
import org.jmqtt.common.bean.Message;
import org.jmqtt.common.bean.MessageHeader;
import org.jmqtt.common.helper.SerializeHelper;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.group.protocol.ClusterRemotingCommand;
import org.jmqtt.group.protocol.ClusterRequestCode;
import org.jmqtt.remoting.util.MessageUtil;
import org.jmqtt.store.RetainMessageStore;
import org.jmqtt.store.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

*/
/**
 * 用于向broker推送符合yunba协议的mqtt消息
 *
 * @author Alex Liu
 * @date 2019/11/13
 *//*

public class YunbaMessageUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.MESSAGE_TRACE);

    private static MessageDispatcher messageDispatcher;
    private static InnerMessageTransfer messageTransfer;
    private static RetainMessageStore retainMessageStore;

    private static final String SYSTEM_CLIENT_ID = "JMQTT_SYS";

*/
/*    private static final String broker = "tcp://127.0.0.1:1883";
    private static final String content = "{\"tranId\":\"3BB5666C81C747BFAAC558651D4954C\",\"commandId\":\"LOOKUP\",\"commandContent\":{}}";

    private static final String topic = "T00000493012";
    private static final String clientId = "PRODUCER_FOR_YUNBA";*//*


    public static void init(BrokerController brokerController){
        logger.info("init yunba message util...");

        if(brokerController == null){
            logger.warn("brokerController is null");
        }
        messageDispatcher = brokerController.getMessageDispatcher();
        retainMessageStore = brokerController.getRetainMessageStore();
        messageTransfer = brokerController.getInnerMessageTransfer();
    }


    public static void pushMessage(String topic , String message , int qos , boolean retain){
        Message publishMessage = new Message();
        publishMessage.setMsgId(MessageUtil.generateMessageId());
        publishMessage.setClientId(SYSTEM_CLIENT_ID);
        publishMessage.setType(Message.Type.PUBLISH);
        publishMessage.setPayload(message.getBytes());

        Map<String,Object> headers = new HashMap<>();
        headers.put(MessageHeader.TOPIC,topic);
        headers.put(MessageHeader.QOS,qos);
        headers.put(MessageHeader.RETAIN,retain);
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
        dispatcherMessage2Cluster(message);
    }

    private static void dispatcherMessage2Cluster(Message message){
        ClusterRemotingCommand remotingCommand = new ClusterRemotingCommand(ClusterRequestCode.SEND_MESSAGE);
        byte[] body = SerializeHelper.serialize(message);
        remotingCommand.setBody(body);
        YunbaMessageUtil.messageTransfer.send2AllNodes(remotingCommand);
    }
}
*/
