package org.jmqtt.common.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author Alex Liu
 * @date 2019/12/11
 */
public class JsonObjectHelper {

    public static String objectToJsonString(Object object){
        return JSONObject.toJSONString(object);
    }

    public static <T> T jsonStringToObject(String jsonStr , Class<T> clazz ){
        return JSON.parseObject(jsonStr , clazz);
    }
}

