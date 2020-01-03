package org.jmqtt.remoting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class RemotingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemotingApplication.class, args);
    }

}
