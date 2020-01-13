package org.jmqtt.store.rocksdb;

import com.alibaba.fastjson.JSONObject;
import org.jmqtt.common.bean.Message;
import org.jmqtt.common.bean.Subscription;
import org.jmqtt.common.helper.JsonObjectHelper;
import org.jmqtt.common.helper.SerializeHelper;
import org.jmqtt.store.WillMessageStore;
import org.jmqtt.store.redis.RedisOfflineMessageStore;
import org.jmqtt.store.redis.RedisRetainMessageStore;
import org.jmqtt.store.redis.RedisSubscriptionStore;
import org.jmqtt.store.redis.RedisWillMessageStore;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class RocksdbStoreTest {

    private RocksDB rocksDB;
    private char sep = 1;

    @Before
    public void before(){
        RocksDB.loadLibrary();
        Options options = new Options();
        options.setCreateIfMissing(true)
                .setWriteBufferSize(64 * SizeUnit.KB)
                .setMaxWriteBufferNumber(3)
                .setMaxBackgroundCompactions(10)
                .setCompressionType(CompressionType.NO_COMPRESSION)
                .setCompactionStyle(CompactionStyle.UNIVERSAL);
        Filter bloomFilter = new BloomFilter(100);
        ReadOptions readOptions = new ReadOptions().setFillCache(false);
        RateLimiter rateLimiter = new RateLimiter(10000000, 10000, 10);

        options.setMemTableConfig(
                new HashSkipListMemTableConfig()
                        .setHeight(4)
                        .setBranchingFactor(4)
                        .setBucketCount(2000000));

        options.setMemTableConfig(
                new HashLinkedListMemTableConfig()
                        .setBucketCount(100000));
        options.setMemTableConfig(
                new VectorMemTableConfig().setReservedSize(10000));

        options.setMemTableConfig(new SkipListMemTableConfig());

        options.setTableFormatConfig(new PlainTableConfig());
        // Plain-Table requires mmap read
        options.setAllowMmapReads(true);

        options.setRateLimiter(rateLimiter);
        final BlockBasedTableConfig table_options = new BlockBasedTableConfig();
        table_options.setBlockCacheSize(64 * SizeUnit.KB)
                .setFilter(bloomFilter)
                .setCacheNumShardBits(6)
                .setBlockSizeDeviation(5)
                .setBlockRestartInterval(10)
                .setCacheIndexAndFilterBlocks(true)
                .setHashIndexAllowCollision(false)
                .setBlockCacheCompressedSize(64 * SizeUnit.KB)
                .setBlockCacheCompressedNumShardBits(10);

        options.setTableFormatConfig(table_options);
        try {
            rocksDB = RocksDB.open(options,"db");
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMap() throws RocksDBException {
        String key = "key1";
        String value = "this is a test value";
        rocksDB.put(key.getBytes(),value.getBytes());
        String getValue = new String(rocksDB.get(key.getBytes()));
        Assert.assertTrue(getValue.equals(value));
    }

    @Test
    public void testHash() throws RocksDBException {
        String key = "hashKey";
        String field1 = "field1";
        String field2 = "field2";
        String value1 = "value1";
        String value2 = "value2";
        hset(key,field1,value1);
        hset(key,field2,value2);
        RocksIterator iterator = rocksDB.newIterator();
        for(iterator.seek(key.getBytes());iterator.isValid();iterator.next()){
            System.out.println(new String(iterator.key()) + "=====" + new String(iterator.value()));
        }
    }


    private void hset(String key,String field,String value) throws RocksDBException {
        String relKey = key + sep + field;
        rocksDB.put(relKey.getBytes(),value.getBytes());
    }

    private void hList(String key,String value) throws RocksDBException {
        rocksDB.put((key + sep + value).getBytes(),value.getBytes());
    }

    @Test
    public void testList() throws RocksDBException {
        String listKey = "listKey";
        for(int i = 0 ; i < 10; i++){
            long rs = rocksDB.getLatestSequenceNumber();
            System.out.println(rs);
            byte[] byteKey = (listKey + sep + rs).getBytes();
            rocksDB.put(byteKey,("val"+i).getBytes());
        }
        RocksIterator iterator = rocksDB.newIterator();

        for(iterator.seek(listKey.getBytes());iterator.isValid();iterator.next()){
            System.out.println(new String(iterator.key()) + "========" + new String(iterator.value()));
        }
    }

    /**
     * 创建连接
     * @return redis实例
     */
    public Jedis createJedis(){
        Jedis jedis = new Jedis("localhost" , 6379);
        //jedis.auth("");
        return jedis;
    }

    @Test
    public void testRedis(){
        System.out.println("-------------------测试redis实例创建-----------------");
        Jedis jedis = createJedis();
        System.out.println(jedis.get("test"));
        System.out.println(jedis.keys("0000006670-000000171144*").size());
        jedis.flushAll();
        /*System.out.println("connect successful!");
        System.out.println("service running: "+jedis.ping());
        System.out.println("-------------------测试字符串-----------------");
        jedis.set("name", "ydfind");
        System.out.println("key name = "+ jedis.get("name"));

        System.out.println("-------------------测试List-----------------");
        jedis.lpush("myList", "item1");
        jedis.lpush("myList", "item2");
        jedis.lpush("myList", "item3");
        // 获取存储的数据并输出
        List<String> list = jedis.lrange("myList", 0 ,2);
        for(int i=0; i<list.size(); i++) {
            System.out.println("item" + (i + 1) + " = "+list.get(i));
        }
        System.out.println("-------------------测试Set-----------------");
        Set<String> keys = jedis.keys("*");
        Iterator<String> it=keys.iterator() ;
        while(it.hasNext()){
            String key = it.next();
            System.out.println(key);
        }*/
    }

    /*@Test
    public void testRedisOfflineMessageStore(){
        RedisOfflineMessageStore redisOfflineMessageStore = new RedisOfflineMessageStore(createJedis());
        String clinetId = "TestClientID";
        Message message1 = new Message();
        message1.setMsgId(1);
        message1.setClientId("111");
        Message message2 = new Message();
        message2.setMsgId(2);
        message2.setClientId("222");
        redisOfflineMessageStore.addOfflineMessage(clinetId , message1);
        redisOfflineMessageStore.addOfflineMessage(clinetId , message2);

        Assert.assertTrue(redisOfflineMessageStore.containOfflineMsg(clinetId));
        //System.out.println(redisOfflineMessageStore.containOfflineMsg(clinetId));
        Collection<Message> collection = redisOfflineMessageStore.getAllOfflineMessage(clinetId);

        Assert.assertTrue(collection.size() == 2);

*//*        for (Message msg : collection) {
            //((JSONObject) msg).parseObject(((JSONObject) msg).getString(), Message.class);
            //Message msg1 = JSONObject.parseObject(JSONObject.toJSONString(msg) , Message.class);
            System.out.println(msg);
        }*//*

        redisOfflineMessageStore.clearOfflineMsgCache(clinetId);
        Collection<Message> collectionAfterClear = redisOfflineMessageStore.getAllOfflineMessage(clinetId);
        Assert.assertTrue(collectionAfterClear == null || collectionAfterClear.size() == 0);


    }*/

  /*  @Test
    public void testRedisRatainMessageStore(){
        RedisRetainMessageStore redisRetainMessageStore = new RedisRetainMessageStore(createJedis());
        String topic1 = "12345678901";
        String topic2 = "12345678902";
        Message message1 = new Message();
        message1.setMsgId(1);
        Message message2 = new Message();
        message2.setMsgId(2);
        redisRetainMessageStore.storeRetainMessage(topic1 , message1);
        redisRetainMessageStore.storeRetainMessage(topic2 , message2);

        Collection<Message> collection = redisRetainMessageStore.getAllRetainMessage();
        Assert.assertTrue(collection.size() == 2);

       *//* for (Message msg : collection) {
            System.out.println(msg);
        }*//*

        redisRetainMessageStore.removeRetainMessage(topic1);

        Collection<Message> collectionAfterDelete = redisRetainMessageStore.getAllRetainMessage();
        Assert.assertTrue(collectionAfterDelete.size() == 1);

        *//*for (Message msg : collectionAfterDelete) {
            System.out.println(msg);
        }*//*

    }*/

/*    @Test
    public void testRedisWillMessageStore(){
        RedisWillMessageStore redisWillMessageStore = new RedisWillMessageStore(createJedis());
        String clinetId = "TestClientID";
        Message message1 = new Message();
        message1.setMsgId(1);
        message1.setClientId("111");
        Message message2 = new Message();
        message2.setMsgId(2);
        message2.setClientId("222");
        redisWillMessageStore.storeWillMessage(clinetId  , message1);
        //System.out.println(redisWillMessageStore.getWillMessage(clinetId));
        //System.out.println(message1);
        //System.out.println(redisWillMessageStore.hasWillMessage(clinetId));
        //System.out.println(redisWillMessageStore.removeWillMessage(clinetId));
        //System.out.println(redisWillMessageStore.hasWillMessage(clinetId));
        Assert.assertTrue(redisWillMessageStore.getWillMessage(clinetId).equals(message1));
        Assert.assertTrue(redisWillMessageStore.hasWillMessage(clinetId));
        Assert.assertTrue(redisWillMessageStore.removeWillMessage(clinetId).equals(message1));
        Assert.assertTrue(redisWillMessageStore.hasWillMessage(clinetId) == false);

    }

    @Test
    public void testRedisSubscriptionStore(){
        RedisSubscriptionStore redisSubscriptionStore = new RedisSubscriptionStore(createJedis());
        String clinetId1 = "TestClientID1";
        String topic1 = "12345678901";
        String topic2 = "12345678902";
        String topic3 = "12345678903";
        Subscription subscription1 = new Subscription(clinetId1 , topic1 , 1);
        Subscription subscription2 = new Subscription(clinetId1 , topic2 , 0);
        Subscription subscription3 = new Subscription(clinetId1 , topic3 , 1);
        redisSubscriptionStore.storeSubscription(clinetId1 , subscription1);
        redisSubscriptionStore.storeSubscription(clinetId1 , subscription2);
        redisSubscriptionStore.storeSubscription(clinetId1 , subscription3);

        Collection<Subscription> collection = redisSubscriptionStore.getSubscriptions(clinetId1);
        Assert.assertTrue(collection.size() == 3);

        *//*for (Subscription sub : collection) {
            System.out.println(sub);
        }*//*

        redisSubscriptionStore.removeSubscription(clinetId1 , topic1);
        Collection<Subscription> collectionAfterRemove = redisSubscriptionStore.getSubscriptions(clinetId1);
        Assert.assertTrue(collectionAfterRemove.size() == 2);

        *//*for (Subscription sub : collectionAfterRemove) {
            System.out.println(sub);
        }*//*

        redisSubscriptionStore.clearSubscription(clinetId1);
        Assert.assertTrue(redisSubscriptionStore.getSubscriptions(clinetId1).size() == 0);
        //System.out.println(redisSubscriptionStore.getSubscriptions(clinetId1).size());
    }

    @Test
    public void testRedisFlowMessageStore(){
        RedisSubscriptionStore redisSubscriptionStore = new RedisSubscriptionStore(createJedis());
        Collection<Subscription> collection = redisSubscriptionStore.getSubscriptions("0000006670-000000171144");
        System.out.println(collection.size());
        for (Subscription sub : collection) {
            System.out.println(sub.getTopic());
        }
    }*/

}