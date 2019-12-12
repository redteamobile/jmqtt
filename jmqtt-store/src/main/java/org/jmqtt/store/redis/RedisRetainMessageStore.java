package org.jmqtt.store.redis;

import org.jmqtt.common.bean.Message;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.store.RetainMessageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * RetainMessage的存储形式为Map<prefix+topic , message的jsonString>
 *
 * @author Alex Liu
 * @date 2019/12/11
 */
public class RedisRetainMessageStore implements RetainMessageStore {

    private static final Logger logger = LoggerFactory.getLogger(RedisRetainMessageStore.class);
    private Jedis jedis;

    public RedisRetainMessageStore(Jedis jedis){
        this.jedis = jedis;
    }

    @Override
    public Collection<Message> getAllRetainMessage() {
        Set<String> keys = jedis.keys(prefixAndTopic("*"));
        Collection<Message> retainMessages = new ArrayList<>();
        for(String key : keys){
            String retainMsgJsonStr = jedis.get(key);
            if(Objects.nonNull(retainMsgJsonStr)){
                retainMessages.add(JsonObjectHelper.jsonStringToObject(retainMsgJsonStr , Message.class));
            }
        }
        return retainMessages;
    }

    @Override
    public void storeRetainMessage(String topic, Message message) {
        this.jedis.set(prefixAndTopic(topic) , JsonObjectHelper.objectToJsonString(message));
    }

    @Override
    public void removeRetainMessage(String topic) {
        this.jedis.del(prefixAndTopic(topic));
    }

    private String prefixAndTopic(String topic){
        return RedisStorePrefix.RETAIN_MESSAGE + topic;
    }
}
