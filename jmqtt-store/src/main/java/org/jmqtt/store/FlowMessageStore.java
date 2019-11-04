package org.jmqtt.store;

import org.jmqtt.common.bean.Message;

import java.util.Collection;

/**
 * 存储与释放过程消息
 */
public interface FlowMessageStore {

    void clearClientFlowCache(String clientId);

    Message getRecMsg(String clientId, String msgId);

    boolean cacheRecMsg(String clientId,Message message);

    Message releaseRecMsg(String clientId,String msgId);

    boolean cacheSendMsg(String clientId,Message message);

    Collection<Message> getAllSendMsg(String clientId);

    boolean releaseSendMsg(String clientId,String msgId);

    boolean containSendMsg(String clientId,String msgId);

}
