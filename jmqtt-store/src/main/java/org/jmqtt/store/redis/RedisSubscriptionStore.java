package org.jmqtt.store.redis;

import org.apache.commons.lang3.StringUtils;
import org.jmqtt.common.bean.Subscription;
import org.jmqtt.common.bean.Topic;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.store.SubscriptionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

/**
 * 存储格式为<perfix + clientId + topic , subscription的jsonStr>
 *
 * @author Alex Liu
 * @date 2019/12/11
 */
public class RedisSubscriptionStore implements SubscriptionStore {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.STORE);

    private RedisTemplate<String , String> redisTemplate;

    public RedisSubscriptionStore(RedisTemplate<String , String> redisTemplate){
        logger.info("RedisSubscriptionStore init...");
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean storeSubscription(String clientId, Subscription subscription) {
        redisTemplate.opsForValue().set(prefixAndClinetIdAndTopic(clientId , subscription.getTopic()) , JsonObjectHelper.objectToJsonString(subscription));
        return true;
    }

    @Override
    public Collection<Subscription> getSubscriptions(String clientId) {
        Set<String> keys = redisTemplate.keys(prefixAndClinetIdAndTopic(clientId , "*"));
        Collection<Subscription> subscriptions = new ArrayList<>();
        for (String key:keys) {
            String subscriptionString = redisTemplate.opsForValue().get(key);
            if(StringUtils.isEmpty(subscriptionString)){
                continue;
            }
            Subscription subscription = JsonObjectHelper.jsonStringToObject(subscriptionString , Subscription.class);
            subscriptions.add(subscription);
        }
        return subscriptions;
    }

    @Override
    public boolean clearSubscription(String clientId) {
        Set<String> keys = redisTemplate.keys(prefixAndClinetIdAndTopic(clientId , "*"));
        for (String key:keys) {
            redisTemplate.delete(key);
        }
        return true;
    }

    @Override
    public boolean removeSubscription(String clientId, String topic) {
        redisTemplate.delete(prefixAndClinetIdAndTopic(clientId , topic));
        return true;
    }

    private String prefixAndClinetId(String clientId){
        return RedisStorePrefix.WILL_MESSAGE + clientId;
    }

    private String prefixAndClinetIdAndTopic(String clientId , String topic){
        return RedisStorePrefix.WILL_MESSAGE + clientId + topic;
    }

}
