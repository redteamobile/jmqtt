package org.jmqtt.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class CommenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommenApplication.class, args);
    }

}
