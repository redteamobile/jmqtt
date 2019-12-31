package org.jmqtt.controller;

import org.jmqtt.broker.BrokerController;
import org.jmqtt.broker.BrokerStartup;
import org.jmqtt.persistent.PersistentApplication;
import org.jmqtt.persistent.asyncTask.AsyncTask;
import org.jmqtt.persistent.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.persistence.Persistence;

@SpringBootApplication
@ComponentScan(value = {"org.jmqtt.controller",
        "org.jmqtt.persistent"})
@EnableAutoConfiguration
@EnableAsync
public class ControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControllerApplication.class, args);
        BrokerStartup.main(args);
    }
}
