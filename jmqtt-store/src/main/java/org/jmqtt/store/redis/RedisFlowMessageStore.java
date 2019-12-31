package org.jmqtt.store.redis;

import org.apache.commons.lang3.StringUtils;
import org.jmqtt.common.bean.Message;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.store.FlowMessageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @author Alex Liu
 * @date 2019/12/10
 */
public class RedisFlowMessageStore implements FlowMessageStore {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.STORE);

    private Jedis jedis;

    public RedisFlowMessageStore(Jedis jedis){
        this.jedis = jedis;
        logger.info("RedisFlowMessageStore init...");
    }

    @Override
    public void clearClientFlowCache(String clientId){
        this.jedis.del(sendPrefixAndClientId(clientId));
        this.jedis.del(recPrefixAndClientId(clientId));
    }

    @Override
    public Message getRecMsg(String clientId, long msgId){
        String recMsgJsonStr = this.jedis.get(recPrefixAndClientIdAndMsgId(clientId , msgId));
        if(StringUtils.isEmpty(recMsgJsonStr)){
            return null;
        }
        return JsonObjectHelper.jsonStringToObject(recMsgJsonStr , Message.class);
    }

    @Override
    public boolean cacheRecMsg(String clientId,Message message){
        this.jedis.set(recPrefixAndClientIdAndMsgId(clientId , message.getMsgId()) , JsonObjectHelper.objectToJsonString(message));
        return true;
    }

    @Override
    public Message releaseRecMsg(String clientId,long msgId){
        Message message = getRecMsg(clientId , msgId);
        if(message == null){
            logger.warn("The message is not exist,clientId={},msgId={}",clientId,msgId);
            return message;
        }
        this.jedis.del(recPrefixAndClientIdAndMsgId(clientId , msgId));
        return message;
    }

    @Override
    public boolean cacheSendMsg(String clientId,Message message){
        this.jedis.set(sendPrefixAndClientIdAndMsgId(clientId , message.getMsgId()) , JsonObjectHelper.objectToJsonString(message));
        return true;
    }

    @Override
    public Collection<Message> getAllSendMsg(String clientId){
        Set<String> keys = this.jedis.keys(sendPrefixAndClientId(clientId + "*"));
        Collection<Message> collection = new ArrayList<>();
        for (String key : keys) {
            String messageString = this.jedis.get(key);
            if(StringUtils.isEmpty(messageString)){
                continue;
            }
            ((ArrayList<Message>) collection).add(JsonObjectHelper.jsonStringToObject(messageString , Message.class));
        }
        return collection;
    }

    @Override
    public boolean releaseSendMsg(String clientId,long msgId){
        this.jedis.del(sendPrefixAndClientIdAndMsgId(clientId , msgId));
        return true;
    }

    @Override
    public boolean containSendMsg(String clientId,long msgId){
        return jedis.exists(sendPrefixAndClientIdAndMsgId(clientId , msgId));
    }

    private String sendPrefixAndClientIdAndMsgId(String clientId,long msgId){
        return RedisStorePrefix.SEND_FLOW_MESSAGE + clientId + msgId;
    }

    private String recPrefixAndClientIdAndMsgId(String clientId,long msgId){
        return RedisStorePrefix.SEND_FLOW_MESSAGE + clientId + msgId;
    }

    private String sendPrefixAndClientId(String clientId){
        return RedisStorePrefix.SEND_FLOW_MESSAGE + clientId;
    }

    private String recPrefixAndClientId(String clientId){
        return RedisStorePrefix.SEND_FLOW_MESSAGE + clientId;
    }
}
