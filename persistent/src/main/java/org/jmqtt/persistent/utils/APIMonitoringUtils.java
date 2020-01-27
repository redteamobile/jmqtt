package org.jmqtt.persistent.utils;

import org.jmqtt.persistent.exception.InvalidParamsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 考虑存储空间及性能问题，未使用同步方法，所有涉及到写内容的方法只能在拦截器的logAround中调用
 *
 * @author Alex Liu
 * @date 2020/01/27
 */
@Component
@EnableScheduling
public class APIMonitoringUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(APIMonitoringUtils.class);

    private static Map<String, Map<Integer, Integer>> countMapForAllAPI = new HashMap<>();

    public static final int SECOUNDS_OF_ONE_DAY = 60*60*24;

    @Value("${api.monitor.keepTime}")
    private int keepTime;

    public static void count(String apiName){
        Map<Integer, Integer> countMapForDesignatedAPI = countMapForAllAPI.get(apiName);
        if(countMapForDesignatedAPI == null){
            countMapForDesignatedAPI = new HashMap<>();
            countMapForAllAPI.put(apiName, countMapForDesignatedAPI);
        }

        Integer time = (int)(new Date().getTime()/1000);
        Integer apiRequestTime = countMapForDesignatedAPI.get(time);

        if(apiRequestTime == null){
            countMapForDesignatedAPI.put(time , new Integer(1));
        }else{
            countMapForDesignatedAPI.put(time , apiRequestTime.intValue() + 1);
        }

    }

    public static Integer getCount(String apiName , int startTime , int endTime){

        Map<Integer , Integer> countMapForDesignatedAPI = countMapForAllAPI.get(apiName);
        if(countMapForDesignatedAPI == null){
            throw new InvalidParamsException(String.format("api ( %s ) is not exist" , apiName));
        }

        int count = 0;
        for(int i = startTime ; i < endTime ; i++){
            if(countMapForDesignatedAPI.get(i) != null){
                count += countMapForDesignatedAPI.get(i);
            }
        }

        return count;
    }

    //每天20点执行定时任务，清空过期的记录，释放内存空间。UTC时区20点为北京时间凌晨4点，业务量较少
    @Scheduled(cron = "0 47 20 * * ? ")
    public void clearAPICountMap(){

        LOGGER.info("start to clear API map...");

        Date nowDate = new Date();
        int startTimestamp = (int)(nowDate.getTime() / 1000) - (keepTime + 1) * SECOUNDS_OF_ONE_DAY;
        int endTimestamp = (int)(nowDate.getTime() / 1000) - keepTime * SECOUNDS_OF_ONE_DAY;

        for (Map.Entry<String ,Map<Integer , Integer>> entry : countMapForAllAPI.entrySet()) {
            //获取到单个API的countMap
            Map<Integer , Integer> clearMap = entry.getValue();
            //清除countMap中指定时间段的值
            for(int i = startTimestamp ; i < endTimestamp ; i++){
                if(clearMap.get(i) != null){
                    clearMap.remove(i);
                }
            }
        }

        LOGGER.info("clear API map finished");
    }

}
