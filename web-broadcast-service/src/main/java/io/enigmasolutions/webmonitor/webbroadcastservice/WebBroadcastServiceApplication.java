package io.enigmasolutions.webmonitor.webbroadcastservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebBroadcastServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebBroadcastServiceApplication.class, args);
    }

}
