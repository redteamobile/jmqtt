package org.jmqtt.persistent.service;

import org.jmqtt.persistent.dao.ClientDao;
import org.jmqtt.persistent.dao.TopicClientDao;
import org.jmqtt.persistent.entity.Client;
import org.jmqtt.persistent.entity.TopicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Alex Liu
 * @date 2019/12/06
 */

@Service
public class TopicClientService {

    @Autowired
    private TopicClientDao topicClientDao;

    public void subscribe(String topic , String clientId){
        TopicClient topicClient = topicClientDao.findByTopicAndClientId(topic , clientId);

        if(topicClient != null){
            topicClient.setLastSubscribeTime(new Date());
            topicClient.setSubscribe(true);
        }else{
            topicClient = TopicClient.builder()
                    .clientId(clientId).topic(topic).createTime(new Date()).lastSubscribeTime(new Date()).subscribe(true)
                    .build();
        }
        topicClientDao.save(topicClient);
    }

    public void unsubscribe(String topic , String clientId){
        TopicClient topicClient = topicClientDao.findByTopicAndClientId(topic , clientId);

        if(topicClient != null){
            topicClient.setLastUnsubscribeTime(new Date());
            topicClient.setSubscribe(false);
            topicClientDao.save(topicClient);
        }
    }
}
