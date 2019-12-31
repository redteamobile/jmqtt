package org.jmqtt.store.redis;

import org.jmqtt.common.config.StoreConfig;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.store.AbstractMqttStore;
import org.jmqtt.store.memory.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

/**
 * 基于redis的MQTT数据存储
 *
 * @author Alex Liu
 * @date 2019/12/10
 */
public class RedisMqttStore extends AbstractMqttStore {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.STORE);

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
        logger.info("Redis Mqtt Store start to init...");
        this.flowMessageStore = new RedisFlowMessageStore(jedis);
        this.willMessageStore = new RedisWillMessageStore(jedis);
        this.retainMessageStore = new RedisRetainMessageStore(jedis);
        this.offlineMessageStore = new RedisOfflineMessageStore(jedis);
        this.subscriptionStore = new RedisSubscriptionStore(jedis);
        this.sessionStore = new RedisSessionStore(jedis);
    }

    @Override
    public void shutdown() {
        jedis.close();
        logger.info("Redis Mqtt Store shutdown");
    }
}
