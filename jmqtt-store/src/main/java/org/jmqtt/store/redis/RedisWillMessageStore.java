package org.jmqtt.store.redis;

import org.jmqtt.common.bean.Message;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.store.WillMessageStore;
import redis.clients.jedis.Jedis;

/**
 * WillMessage的存储形式为Map<prefix+clientId , message的jsonString>
 *
 * @author Alex Liu
 * @date 2019/12/11
 */
public class RedisWillMessageStore implements WillMessageStore {

    private Jedis jedis;

    public RedisWillMessageStore(Jedis jedis){
        this.jedis = jedis;
    }

    @Override
    public Message getWillMessage(String clientId) {
        return JsonObjectHelper.jsonStringToObject(jedis.get(prefixAndClinetId(clientId)) , Message.class);
    }

    @Override
    public boolean hasWillMessage(String clientId) {
        return jedis.exists(prefixAndClinetId(clientId));
    }

    @Override
    public void storeWillMessage(String clientId, Message message) {
        jedis.set(prefixAndClinetId(clientId) , JsonObjectHelper.objectToJsonString(message));
    }

    @Override
    public Message removeWillMessage(String clientId) {
        Message message = getWillMessage(clientId);
        jedis.del(prefixAndClinetId(clientId));
        return message;
    }

    private String prefixAndClinetId(String clientId){
        return RedisStorePrefix.WILL_MESSAGE + clientId;
    }
}
