package org.jmqtt.persistent.asyncTask;

import org.jmqtt.common.log.LoggerName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author Alex Liu
 * @date 2019/12/06
 */
@Component
public class AsyncTask {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.MESSAGE_TRACE);

  /*  @Autowired
    private TopicClientService topicClientService;
    @Autowired
    private ClientService clientService;*/

    @Async("brokerAsyncPool")
    public void subscribe(String topic , String clientId){
        logger.info("start to subscribe TopicClient...");
        //topicClientService.subscribe(topic , clientId);
    }

    @Async("brokerAsyncPool")
    public void unsubscribe(String topic , String clientId){
        logger.info("start to unsubscribe TopicClient...");
        //topicClientService.unsubscribe(topic , clientId);
    }

    @Async("brokerAsyncPool")
    public void disconnect(String clientId){
        logger.info("client {} disconnect ..." , clientId);
        //clientService.disconnect(clientId);
    }

    @Async("brokerAsyncPool")
    public void connect(String clientId){
        logger.info("client {} connect ..." , clientId);
        //clientService.connect(clientId);
    }

}
