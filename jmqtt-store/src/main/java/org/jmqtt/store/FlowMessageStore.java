package org.jmqtt.store;

import org.jmqtt.common.bean.Message;

import java.util.Collection;

/**
 * 存储与释放过程消息
 */
public interface FlowMessageStore {

    void clearClientFlowCache(String clientId);

    Message getRecMsg(String clientId, long msgId);

    boolean cacheRecMsg(String clientId,Message message);

    Message releaseRecMsg(String clientId,long msgId);

    boolean cacheSendMsg(String clientId,Message message);

    Collection<Message> getAllSendMsg(String clientId);

    boolean releaseSendMsg(String clientId,long msgId);

    boolean containSendMsg(String clientId,long msgId);

}
