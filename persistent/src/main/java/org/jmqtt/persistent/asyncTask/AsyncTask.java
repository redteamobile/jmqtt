package org.jmqtt.persistent.asyncTask;

import org.jmqtt.common.log.LoggerName;
import org.jmqtt.persistent.service.PresenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Alex Liu
 * @date 2019/12/06
 */
@Component
public class AsyncTask {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.MESSAGE_TRACE);

    @Autowired
    private RedisTemplate<String , String> redisTemplate;

    @Autowired
    private PresenceService presenceService;

    @Async("brokerAsyncPool")
    public void subscribe(String topic , String clientId){
        logger.info("start to subscribe TopicClient...");
        presenceService.subscribe(topic , clientId);
    }

    @Async("brokerAsyncPool")
    public void unsubscribe(String topic , String clientId){
        logger.info("start to unsubscribe TopicClient...");
        presenceService.unsubscribe(topic , clientId);
    }

    @Async("brokerAsyncPool")
    public void disconnect(String clientId , String reason){
        logger.info("client {} disconnect ..." , clientId);
        presenceService.disconnect(clientId , reason);
        redisTemplate.delete(addClinetCountPrefix(clientId));
    }

    @Async("brokerAsyncPool")
    public void connect(String clientId){
        logger.info("client {} connect ..." , clientId);
        presenceService.connect(clientId);
        redisTemplate.opsForValue().set(addClinetCountPrefix(clientId) , "");
    }

    private String addClinetCountPrefix(String clientId){
        return "clientCount" + clientId;
    }

}
