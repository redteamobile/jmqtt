package org.jmqtt.store.redis;

import org.apache.commons.lang3.StringUtils;
import org.jmqtt.common.bean.Message;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.store.FlowMessageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * @author Alex Liu
 * @date 2019/12/10
 */
public class RedisFlowMessageStore implements FlowMessageStore {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.STORE);

    private RedisTemplate<String , String> redisTemplate;

    public RedisFlowMessageStore(RedisTemplate<String , String> redisTemplate){
        this.redisTemplate = redisTemplate;
        logger.info("RedisFlowMessageStore init...");
    }

    @Override
    public void clearClientFlowCache(String clientId){
        this.redisTemplate.delete(sendPrefixAndClientId(clientId));
        this.redisTemplate.delete(recPrefixAndClientId(clientId));
    }

    @Override
    public Message getRecMsg(String clientId, long msgId){
        String recMsgJsonStr = redisTemplate.opsForValue().get(recPrefixAndClientIdAndMsgId(clientId , msgId));
        if(StringUtils.isEmpty(recMsgJsonStr)){
            return null;
        }
        return JsonObjectHelper.jsonStringToObject(recMsgJsonStr , Message.class);
    }

    @Override
    public boolean cacheRecMsg(String clientId,Message message){
        redisTemplate.opsForValue().set(recPrefixAndClientIdAndMsgId(clientId , message.getMsgId()) , JsonObjectHelper.objectToJsonString(message));
        return true;
    }

    @Override
    public Message releaseRecMsg(String clientId,long msgId){
        Message message = getRecMsg(clientId , msgId);
        if(message == null){
            logger.warn("The message is not exist,clientId={},msgId={}",clientId,msgId);
            return message;
        }
        this.redisTemplate.delete(recPrefixAndClientIdAndMsgId(clientId , msgId));
        return message;
    }

    @Override
    public boolean cacheSendMsg(String clientId,Message message){
        redisTemplate.opsForValue().set(sendPrefixAndClientIdAndMsgId(clientId , message.getMsgId()) , JsonObjectHelper.objectToJsonString(message));
        return true;
    }

    @Override
    public Collection<Message> getAllSendMsg(String clientId){
        Set<String> keys = this.redisTemplate.keys(sendPrefixAndClientId(clientId + "*"));
        Collection<Message> collection = new ArrayList<>();
        for (String key : keys) {
            String messageString = this.redisTemplate.opsForValue().get(key);
            if(StringUtils.isEmpty(messageString)){
                continue;
            }
            ((ArrayList<Message>) collection).add(JsonObjectHelper.jsonStringToObject(messageString , Message.class));
        }
        return collection;
    }

    @Override
    public boolean releaseSendMsg(String clientId,long msgId){
        this.redisTemplate.delete(sendPrefixAndClientIdAndMsgId(clientId , msgId));
        return true;
    }

    @Override
    public boolean containSendMsg(String clientId,long msgId){
        return redisTemplate.hasKey(sendPrefixAndClientIdAndMsgId(clientId , msgId));
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
