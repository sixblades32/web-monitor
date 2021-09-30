package io.enigmasolutions.staffmanager;

import io.enigmasolutions.staffmanager.services.discord.EventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class StaffManagerApplication {

  private static EventListener eventListener;

  StaffManagerApplication(EventListener eventListener) {
    StaffManagerApplication.eventListener = eventListener;
  }

  public static void main(String[] args) {
    SpringApplication.run(StaffManagerApplication.class, args);
    eventListener.login().block();
  }

}
