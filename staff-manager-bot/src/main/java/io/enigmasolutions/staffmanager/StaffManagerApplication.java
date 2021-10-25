package io.enigmasolutions.staffmanager;

import io.enigmasolutions.staffmanager.services.discord.listeners.EventListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class StaffManagerApplication {

  private static EventListener eventListener;
  public static ApplicationContext springContext;

  StaffManagerApplication(EventListener eventListener) {
    StaffManagerApplication.eventListener = eventListener;
  }

  public static void main(String[] args) {
    springContext = new SpringApplicationBuilder(StaffManagerApplication.class)
            .build()
            .run(args);

    eventListener.login(springContext).block();
  }
}
