package org.jmqtt.store.redis;

import org.jmqtt.common.bean.Message;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.store.WillMessageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

/**
 * WillMessage的存储形式为Map<prefix+clientId , message的jsonString>
 *
 * @author Alex Liu
 * @date 2019/12/11
 */
public class RedisWillMessageStore implements WillMessageStore {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.STORE);

    private RedisTemplate<String , String> redisTemplate;

    public RedisWillMessageStore(RedisTemplate<String , String> redisTemplate){
        logger.info("RedisWillMessageStore init...");
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Message getWillMessage(String clientId) {
        return JsonObjectHelper.jsonStringToObject(redisTemplate.opsForValue().get(prefixAndClinetId(clientId)) , Message.class);
    }

    @Override
    public boolean hasWillMessage(String clientId) {
        return redisTemplate.hasKey(prefixAndClinetId(clientId));
    }

    @Override
    public void storeWillMessage(String clientId, Message message) {
        redisTemplate.opsForValue().set(prefixAndClinetId(clientId) , JsonObjectHelper.objectToJsonString(message));
    }

    @Override
    public Message removeWillMessage(String clientId) {
        Message message = getWillMessage(clientId);
        redisTemplate.delete(prefixAndClinetId(clientId));
        return message;
    }

    private String prefixAndClinetId(String clientId){
        return RedisStorePrefix.WILL_MESSAGE + clientId;
    }
}
