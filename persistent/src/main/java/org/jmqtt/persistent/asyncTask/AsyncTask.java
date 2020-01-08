package org.jmqtt.persistent.asyncTask;

import org.jmqtt.common.log.LoggerName;
import org.jmqtt.persistent.service.PresentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PresentService presentService;

    @Async("brokerAsyncPool")
    public void subscribe(String topic , String clientId){
        logger.info("start to subscribe TopicClient...");
        presentService.subscribe(topic , clientId);
    }

    @Async("brokerAsyncPool")
    public void unsubscribe(String topic , String clientId){
        logger.info("start to unsubscribe TopicClient...");
        presentService.unsubscribe(topic , clientId);
    }

    @Async("brokerAsyncPool")
    public void disconnect(String clientId , String reason){
        logger.info("client {} disconnect ..." , clientId);
        presentService.disconnect(clientId , reason);
    }

    @Async("brokerAsyncPool")
    public void connect(String clientId){
        logger.info("client {} connect ..." , clientId);
        presentService.connect(clientId);
    }

}
