package io.enigmasolutions.stuffmanager;

import io.enigmasolutions.stuffmanager.services.discord.EventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class StuffManagerApplication {

    private static EventListener eventListener;

    StuffManagerApplication(EventListener eventListener){
        StuffManagerApplication.eventListener = eventListener;
    }

    public static void main(String[] args) {
        SpringApplication.run(StuffManagerApplication.class, args);
        eventListener.login().block();
    }

}
