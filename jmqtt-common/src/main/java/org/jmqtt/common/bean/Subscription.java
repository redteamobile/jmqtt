package org.jmqtt.common.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class Subscription {
    private String clientId;
    private int qos;
    private String topic;

    public Subscription(String clientId,String topic,int qos){
        this.clientId = clientId;
        this.topic = topic;
        this.qos = qos;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("clientId", clientId)
                .append("qos", qos)
                .append("topic", topic)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(topic, that.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, topic);
    }
}
