package org.jmqtt.persistent.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alex Liu
 * @date 2020/01/08
 */
public class PresentToCthulhuReq implements Serializable {
    private String action;//no comment
    @JsonProperty("client_id")
    private String clientId;//no comment
    @NotBlank
    private String username;//no comment
    private String messageId;//no comment
    private String topic;//no comment
    @JsonProperty("conn_ack")
    private String connAck;//no comment
    private String reason;//no comment
    @JsonProperty("from_client_id")
    private String fromClientId;//no comment
    @JsonProperty("from_username")
    private String fromUsername;//no comment
    @Min(0)
    @Max(2)
    private Integer qos;//no comment
    private Boolean retain;//no comment
    @NotBlank
    private String payload;//no comment
    private Long ts;
    private Date timestamp;
    private Date createTime;//no comment

    private String parentUsername;
    private Date lteCreateTime;
    private Date gteCreateTime;
    private List<String> usernames = new ArrayList<>();


    public static PresentToCthulhuReq build() {
        return new PresentToCthulhuReq();
    }

    public Long getTs() {
        return ts;
    }

    public PresentToCthulhuReq setTs(Long ts) {
        this.ts = ts;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("action", action)
                .add("clientId", clientId)
                .add("username", username)
                .add("messageId", messageId)
                .add("topic", topic)
                .add("connAck", connAck)
                .add("reason", reason)
                .add("fromClientId", fromClientId)
                .add("fromUsername", fromUsername)
                .add("qos", qos)
                .add("retain", retain)
                .add("payload", payload)
                .add("ts", ts)
                .add("timestamp", timestamp)
                .add("createTime", createTime)
                .add("parentUsername", parentUsername)
                .add("lteCreateTime", lteCreateTime)
                .add("gteCreateTime", gteCreateTime)
                .add("usernames", usernames)
                .toString();
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public PresentToCthulhuReq setUsernames(List<String> usernames) {
        this.usernames = usernames;
        return this;
    }

    public String getParentUsername() {
        return parentUsername;
    }

    public PresentToCthulhuReq setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
        return this;
    }

    public Date getLteCreateTime() {
        return lteCreateTime;
    }

    public PresentToCthulhuReq setLteCreateTime(Date lteCreateTime) {
        this.lteCreateTime = lteCreateTime;
        return this;
    }

    public Date getGteCreateTime() {
        return gteCreateTime;
    }

    public PresentToCthulhuReq setGteCreateTime(Date gteCreateTime) {
        this.gteCreateTime = gteCreateTime;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public PresentToCthulhuReq setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public PresentToCthulhuReq setAction(String action) {
        this.action = action == null ? null : action.trim();
        return this;
    }
    public String getAction() {
        return this.action;
    }

    public PresentToCthulhuReq setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
        return this;
    }
    public String getClientId() {
        return this.clientId;
    }

    public PresentToCthulhuReq setUsername(String username) {
        this.username = username == null ? null : username.trim();
        return this;
    }
    public String getUsername() {
        return this.username;
    }

    public PresentToCthulhuReq setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
        return this;
    }
    public String getMessageId() {
        return this.messageId;
    }

    public PresentToCthulhuReq setTopic(String topic) {
        this.topic = topic == null ? null : topic.trim();
        return this;
    }
    public String getTopic() {
        return this.topic;
    }

    public PresentToCthulhuReq setConnAck(String connAck) {
        this.connAck = connAck == null ? null : connAck.trim();
        return this;
    }
    public String getConnAck() {
        return this.connAck;
    }

    public PresentToCthulhuReq setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
        return this;
    }
    public String getReason() {
        return this.reason;
    }

    public PresentToCthulhuReq setFromClientId(String fromClientId) {
        this.fromClientId = fromClientId == null ? null : fromClientId.trim();
        return this;
    }
    public String getFromClientId() {
        return this.fromClientId;
    }

    public PresentToCthulhuReq setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername == null ? null : fromUsername.trim();
        return this;
    }
    public String getFromUsername() {
        return this.fromUsername;
    }

    public PresentToCthulhuReq setQos(Integer qos) {
        this.qos = qos;
        return this;
    }
    public Integer getQos() {
        return this.qos;
    }

    public PresentToCthulhuReq setRetain(Boolean retain) {
        this.retain = retain;
        return this;
    }
    public Boolean getRetain() {
        return this.retain;
    }

    public PresentToCthulhuReq setPayload(String payload) {
        this.payload = payload == null ? null : payload.trim();
        return this;
    }
    public String getPayload() {
        return this.payload;
    }

    public PresentToCthulhuReq setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
}
