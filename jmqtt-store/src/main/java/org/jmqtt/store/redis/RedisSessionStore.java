package org.jmqtt.store.redis;

import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.store.SessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Objects;

/**
 * Session的存储形式为Map<prefix+clientId , Session的jsonString>
 *
 * @author Alex Liu
 * @date 2019/12/11
 */
public class RedisSessionStore implements SessionStore {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.STORE);


    private Jedis jedis;

    public RedisSessionStore(Jedis jedis){
        logger.info("RedisSessionStore init...");
        this.jedis = jedis;
    }

    @Override
    public boolean containSession(String clientId) {
        return jedis.exists(prefixAndTopic(clientId));
    }

    @Override
    public Object setSession(String clientId, Object obj) {
        return jedis.set(prefixAndTopic(clientId) , JsonObjectHelper.objectToJsonString(obj));
    }

    @Override
    public Object getLastSession(String clientId) {
        String sessionString = this.jedis.get(prefixAndTopic(clientId));
        if(Objects.nonNull(sessionString)){
            return JsonObjectHelper.jsonStringToObject(sessionString , Object.class);
        }
        return null;
    }

    @Override
    public boolean clearSession(String clientId) {
        jedis.del(prefixAndTopic(clientId));
        return true;
    }

    private String prefixAndTopic(String topic){
        return RedisStorePrefix.SESSION + topic;
    }
}
