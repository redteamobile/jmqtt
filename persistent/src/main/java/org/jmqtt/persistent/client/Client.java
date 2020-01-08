package org.jmqtt.persistent.client;

import feign.Feign;
import feign.Headers;
import feign.Logger;
import feign.RequestLine;
import feign.Retryer;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import org.jmqtt.common.log.LoggerName;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author Alex Liu
 * @date 2020/01/08
 */
@FeignClient
public interface Client {

    org.slf4j.Logger logger = LoggerFactory.getLogger(LoggerName.MESSAGE_TRACE);


    @Headers({"Content-Type: application/json"})
    @RequestLine("POST")
    @jdk.nashorn.internal.runtime.logging.Logger
    void presentToCthulhu(Object object);

    static Client build(String url, Boolean encoder) {
        Feign.Builder builder = Feign.builder()
                .decoder(new GsonDecoder())
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger())
                .retryer(new Retryer.Default());

        if (encoder) {
            builder.encoder(new GsonEncoder());
        }
        return builder.target(Client.class, url);
    }
}
