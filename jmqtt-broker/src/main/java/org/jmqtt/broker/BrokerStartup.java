package org.jmqtt.broker;

import org.jmqtt.common.config.BrokerConfig;
import org.jmqtt.common.config.ClusterConfig;
import org.jmqtt.common.config.NettyConfig;
import org.jmqtt.common.config.StoreConfig;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.persistent.utils.PropertiesUtils;
import org.jmqtt.persistent.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class BrokerStartup {

    private static final Logger logger = LoggerFactory.getLogger(LoggerName.BROKER);

    public static void main(String[] args) {
        try {
            BrokerController brokerController = start(args);
            YunbaMessageUtil.init(brokerController);
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
        setStoreConfig(storeConfig);

        //TODO：后续这个也改成配置文件配置
        ClusterConfig clusterConfig =new ClusterConfig();

        BrokerController brokerController = new BrokerController(brokerConfig,nettyConfig, storeConfig, clusterConfig);
        brokerController.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                brokerController.shutdown();
            }
        }));

        return brokerController;
    }

    private static void setStoreConfig(StoreConfig storeConfig){
        storeConfig.setStoreType(Integer.valueOf(PropertiesUtils.getPropertiesValue("${store.type}")));
        if(storeConfig.getStoreType() == 2){
            storeConfig.setNodes(PropertiesUtils.getPropertiesValue("${spring.redis.host}"));
            storeConfig.setPassword(PropertiesUtils.getPropertiesValue("${spring.redis.password}"));
        }
    }

}
