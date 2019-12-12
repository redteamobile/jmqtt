package org.jmqtt.store.redis;

import org.apache.commons.lang3.StringUtils;
import org.jmqtt.common.bean.Subscription;
import org.jmqtt.common.bean.Topic;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.store.SubscriptionStore;
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

    private Jedis jedis;

    public RedisSubscriptionStore(Jedis jedis){
        this.jedis = jedis;
    }

    @Override
    public boolean storeSubscription(String clientId, Subscription subscription) {
        jedis.set(prefixAndClinetIdAndTopic(clientId , subscription.getTopic()) , JsonObjectHelper.objectToJsonString(subscription));
        return true;
    }

    @Override
    public Collection<Subscription> getSubscriptions(String clientId) {
        Set<String> keys = jedis.keys(prefixAndClinetIdAndTopic(clientId , "*"));
        Collection<Subscription> subscriptions = new ArrayList<>();
        for (String key:keys) {
            String subscriptionString = jedis.get(key);
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
        Set<String> keys = jedis.keys(prefixAndClinetIdAndTopic(clientId , "*"));
        for (String key:keys) {
            jedis.del(key);
        }
        return true;
    }

    @Override
    public boolean removeSubscription(String clientId, String topic) {
        jedis.del(prefixAndClinetIdAndTopic(clientId , topic));
        return true;
    }

    private String prefixAndClinetId(String clientId){
        return RedisStorePrefix.WILL_MESSAGE + clientId;
    }

    private String prefixAndClinetIdAndTopic(String clientId , String topic){
        return RedisStorePrefix.WILL_MESSAGE + clientId + topic;
    }

}
