package org.jmqtt.broker;

import org.jmqtt.common.config.BrokerConfig;
import org.jmqtt.common.config.ClusterConfig;
import org.jmqtt.common.config.NettyConfig;
import org.jmqtt.common.config.StoreConfig;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.persistent.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrokerStartup {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.BROKER);

    public static void main(String[] args) {
        try {
            BrokerController brokerController = start(args);
            //YunbaMessageUtil.init(brokerController);
        } catch (Exception e) {
            logger.error("Jmqtt start failure,cause = " + e);
            logger.error(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static BrokerController start(String[] args) throws Exception {

        BrokerConfig brokerConfig = new BrokerConfig();
        NettyConfig nettyConfig = new NettyConfig();
        StoreConfig storeConfig = new StoreConfig();
        ClusterConfig clusterConfig =new ClusterConfig();

    /*    //把redis配置移植到springboot配置文件中
        String redisNode = PropertiesUtils.getPropertiesValue("${spring.redis.host}");
        if(redisNode != null){
            storeConfig.setNodes(redisNode);
            storeConfig.setPassword(PropertiesUtils.getPropertiesValue("${spring.redis.password}"));
        }*/

        BrokerController brokerController = new BrokerController(brokerConfig,nettyConfig, storeConfig, clusterConfig);
        BrokerController.instance = brokerController;
        brokerController.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                brokerController.shutdown();
            }
        }));

        return brokerController;
    }

}
