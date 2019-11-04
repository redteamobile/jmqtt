package org.jmqtt.java;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Producer {
    private static final String broker = "tcp://127.0.0.1:1883";
    private static final String content = "abcdef1234567";
    private static final int qos = 1;
    private static final String topic = ",yali";
    private static final String clientId = "MQTT_PUB_CLIENT";

    public static void main(String[] args) throws MqttException, InterruptedException {
        MqttClient pubClient = getMqttClient();
        pubClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("Connect lost,do some thing to solve it");
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println("From topic: " + s);
                System.out.println("Message content: " + new String(mqttMessage.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        for(int i = 0; i < 3; i++){
            MqttMessage mqttMessage = getMqttMessage();
            pubClient.publish(topic,mqttMessage);
            System.out.println("Send message success.");
        }
    }

    private static MqttMessage getMqttMessage(){
        MqttMessage mqttMessage = new MqttMessage(content.getBytes());
        mqttMessage.setQos(qos);
        return mqttMessage;
    }

    private static MqttClient getMqttClient(){
        try {
            MqttClient pubClient = new MqttClient(broker,clientId,new MemoryPersistence());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setWill("lwt","this is a will message".getBytes(),1,false);
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
