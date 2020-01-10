package org.jmqtt.store.redis;

import org.jmqtt.common.config.StoreConfig;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.store.AbstractMqttStore;
import org.jmqtt.store.memory.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 基于redis的MQTT数据存储
 * TODO：后续考虑一种更好的redis操作办法，使用连接池，每次操作都使用空余的连接，确保不会冲突
 *
 * @author Alex Liu
 * @date 2019/12/10
 */
public class RedisMqttStore extends AbstractMqttStore {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.STORE);

    private StoreConfig storeConfig;

    public RedisMqttStore(StoreConfig storeConfig){
       this.storeConfig = storeConfig;
    }

    private Jedis generateJedis(StoreConfig storeConfig){
        Jedis jedis = new Jedis(storeConfig.getNodes());

        if(!(storeConfig.getPassword() == null || storeConfig.getPassword().trim().equals(""))){
            jedis.auth(storeConfig.getPassword().trim());
        }

        return jedis;
    }

    @Override
    public void init(){
        logger.info("Redis Mqtt Store start to init...");
        this.flowMessageStore = new RedisFlowMessageStore(generateJedis(storeConfig));
        this.willMessageStore = new RedisWillMessageStore(generateJedis(storeConfig));
        this.retainMessageStore = new RedisRetainMessageStore(generateJedis(storeConfig));
        this.offlineMessageStore = new RedisOfflineMessageStore(generateJedis(storeConfig));
        this.subscriptionStore = new RedisSubscriptionStore(generateJedis(storeConfig));
        this.sessionStore = new RedisSessionStore(generateJedis(storeConfig));
    }

    @Override
    public void shutdown() {
        logger.info("Redis Mqtt Store shutdown");
    }
}
