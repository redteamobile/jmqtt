package org.jmqtt.store.redis;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jmqtt.common.bean.Message;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.common.helper.SerializeHelper;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.store.OfflineMessageStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 数据存入格式为<prefix + clientID + MessageId , message.jsonStr>
 *
 * @author Alex Liu
 * @date 2019/12/11
 */
public class RedisOfflineMessageStore implements OfflineMessageStore {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.STORE);
    private Jedis jedis;

    public RedisOfflineMessageStore(Jedis jedis){
        logger.info("RedisOfflineMessageStore init...");
        this.jedis = jedis;
    }

    @Override
    public void clearOfflineMsgCache(String clientId) {
        Set<String> keys = this.jedis.keys(prefixAndClinetId(clientId + "*"));
        for (String key : keys) {
            this.jedis.del(key);
        }
    }

    @Override
    public boolean containOfflineMsg(String clientId) {
        Set<String> keys = this.jedis.keys(prefixAndClinetId(clientId + "*"));
        if(keys == null || keys.size() == 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean addOfflineMessage(String clientId, Message message) {
        String key = prefixAndClinetIdAndMessageId(clientId , message.getMsgId());
        this.jedis.set(key , JsonObjectHelper.objectToJsonString(message));
        return true;
    }

    @Override
    public Collection<Message> getAllOfflineMessage(String clientId) {
        Set<String> keys = this.jedis.keys(prefixAndClinetId(clientId + "*"));
        Collection<Message> messages = new ArrayList<>();
        for (String key : keys) {
            String messageString = this.jedis.get(key);
            //判断是否为空，防止有其他线程删除了这条记录
            if(StringUtils.isEmpty(messageString)){
                continue;
            }
            Message message = JsonObjectHelper.jsonStringToObject(messageString , Message.class);
            ((ArrayList<Message>) messages).add(message);
        }
        return messages;
    }

    private String prefixAndClinetId(String clientId){
        return RedisStorePrefix.OFFLINE_MESSAGE + clientId;
    }

    private String prefixAndClinetIdAndMessageId(String clientId , long messageId){
        return RedisStorePrefix.OFFLINE_MESSAGE + clientId + messageId;
    }
}
