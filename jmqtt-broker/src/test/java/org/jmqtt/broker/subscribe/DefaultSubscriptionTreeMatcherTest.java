package org.jmqtt.broker.subscribe;

import javafx.application.Application;
import org.jmqtt.common.bean.Subscription;
import org.jmqtt.persistent.asyncTask.AsyncTask;
import org.jmqtt.persistent.utils.SpringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
/*@RunWith(SpringRunner.class)*/
public class DefaultSubscriptionTreeMatcherTest {

    private SubscriptionMatcher subscriptionMatcher;

    @Autowired
    private AsyncTask asyncTask;

    @Before
    public void init(){
        this.subscriptionMatcher = new DefaultSubscriptionTreeMatcher();
    }

    @Test
    public void subscribe() {
        String topic = "MQTT_TEST/2/+/E/#";
        String clientId = "clientid_test";
        Subscription subscription = new Subscription(clientId,topic,1);
        Assert.assertTrue(this.subscriptionMatcher.subscribe(subscription));
    }

    @Test
    public void test(){
        byte b = (byte) 0x12;
        System.out.println();
    }

    @Test
    public void testAsy() throws Exception{
        asyncTask.unsubscribe("12345" , "LTHTest");
        Thread.currentThread().sleep(30000);
    }

    @Test
    public void testGet(){
        AsyncTask asyncTask = SpringUtil.getBean(AsyncTask.class);
        asyncTask.disconnect("lthtest");
    }

    @Test
    public void match() {
        String[] topics = new String[4];
        String[] clientIds = new String[10];
        for(int i = 0; i < 4; i++){
            topics[i] = "T_TEST_" + i;
        }
        for(int i = 0; i < 10; i++){
            clientIds[i] = "C_clientId_" + i;
            String topic = topics[i%4];
            Subscription subscription = new Subscription(clientIds[i],topic,1);
            subscriptionMatcher.subscribe(subscription);
        }
        Set<Subscription> subscriptions = new HashSet<>();
        for(int i = 0; i < 4; i++){
            Set<Subscription> tempCientIds = subscriptionMatcher.match("T_TEST_"+i);
            subscriptions.addAll(tempCientIds);
        }
    }
}