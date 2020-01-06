package org.jmqtt.persistent.service;

import org.jmqtt.persistent.client.Client;
import org.jmqtt.persistent.dao.TopicClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Alex Liu
 * @date 2019/12/06
 */

@Service
public class TopicClientService {

    @Autowired
    private TopicClientDao topicClientDao;

    public void subscribe(String topic , String clientId){
        //发送消息到Cthulhu
        Client.build()
    }

    public void unsubscribe(String topic , String clientId){

    }
}
