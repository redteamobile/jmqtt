package org.jmqtt.store.redis;

import org.jmqtt.common.config.StoreConfig;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.persistent.utils.SpringUtil;
import org.jmqtt.store.AbstractMqttStore;
import org.jmqtt.store.memory.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
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

    private RedisTemplate<String , String> redisTemplate;

    public RedisMqttStore(){
        RedisTemplate<String, String> redisTemplate = (RedisTemplate<String, String>)SpringUtil.getBean("redisTemplate");
        this.redisTemplate = redisTemplate;

    }

    @Override
    public void init(){
        logger.info("Redis Mqtt Store start to init...");
        this.flowMessageStore = new RedisFlowMessageStore(redisTemplate);
        this.willMessageStore = new RedisWillMessageStore(redisTemplate);
        this.retainMessageStore = new RedisRetainMessageStore(redisTemplate);
        this.offlineMessageStore = new RedisOfflineMessageStore(redisTemplate);
        this.subscriptionStore = new RedisSubscriptionStore(redisTemplate);
        this.sessionStore = new RedisSessionStore(redisTemplate);
    }

    @Override
    public void shutdown() {
        logger.info("Redis Mqtt Store shutdown");
    }
}
