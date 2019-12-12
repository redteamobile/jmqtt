package org.jmqtt.store.redis;

import org.jmqtt.common.config.StoreConfig;
import org.jmqtt.store.AbstractMqttStore;
import org.jmqtt.store.memory.*;
import redis.clients.jedis.Jedis;

/**
 * 基于redis的MQTT数据存储
 *
 * @author Alex Liu
 * @date 2019/12/10
 */
public class RedisMqttStore extends AbstractMqttStore {

    private Jedis jedis;

    public RedisMqttStore(StoreConfig storeConfig){
        Jedis jedis = new Jedis(storeConfig.getNodes());

        if(!(storeConfig.getPassword() == null || storeConfig.getPassword().trim().equals(""))){
            jedis.auth(storeConfig.getPassword().trim());
        }

        this.jedis = jedis;
    }

    @Override
    public void init(){
        this.flowMessageStore = new RedisFlowMessageStore(jedis);
        this.willMessageStore = new RedisWillMessageStore(jedis);
        this.retainMessageStore = new RedisRetainMessageStore(jedis);
        this.offlineMessageStore = new RedisOfflineMessageStore(jedis);
        this.subscriptionStore = new RedisSubscriptionStore(jedis);
        this.sessionStore = new RedisSessionStore(jedis);
    }

    @Override
    public void shutdown() {

    }
}
