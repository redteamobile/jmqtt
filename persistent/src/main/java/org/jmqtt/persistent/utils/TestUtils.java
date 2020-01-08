package org.jmqtt.persistent.utils;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.util.AlternativeJdkIdGenerator;
import org.springframework.util.IdGenerator;

import java.util.Random;

/**
 * @author Alex Liu
 * @date 2020/01/06
 */
public class TestUtils {


    private static IdGenerator idGenerator = new AlternativeJdkIdGenerator();

    private static final String broker = "tcp://107.150.119.156:1883";
    private static final String topic = "MQTT/+";

    public void pressureUtil(int num) throws Exception{
        for(int i = 0 ; i < num ; i++){
            MqttClient client = getMqttClient();
            Thread.sleep(1000);
        }

    }


    private  String generateMatchingId() {
        return idGenerator.generateId().toString().replaceAll("-", "").toUpperCase();
    }

    private  MqttClient getMqttClient(){
        try {
            MqttClient pubClient = new MqttClient(broker, generateMatchingId(),new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            pubClient.connect(connectOptions);
            return pubClient;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return null;
    }
}
