package org.jmqtt.controller;

import org.jmqtt.broker.BrokerStartup;
import org.jmqtt.persistent.service.PresentService;
import org.jmqtt.persistent.utils.SpringUtil;
import org.jmqtt.persistent.utils.TestUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@ComponentScan(value = {"org.jmqtt.controller",
        "org.jmqtt.persistent"})
@EnableAutoConfiguration
@EnableAsync
public class ControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
        BrokerStartup.main(args);
        SpringUtil.getBean(PresentService.class).connect("89023022000010000000000001555428");
        /*TestUtils testUtils = new TestUtils();
        try {
            testUtils.pressureUtil(1);
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }
}
