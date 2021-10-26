package io.enigmasolutions.staffmanagerbot.services;

import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerDiscordGuildCacheScheduler {

  private final CustomerDiscordGuildCache customerDiscordGuildCache;

  public CustomerDiscordGuildCacheScheduler(CustomerDiscordGuildCache customerDiscordGuildCache) {
    this.customerDiscordGuildCache = customerDiscordGuildCache;
  }

  @Scheduled(fixedRate = 15000)
  public void scheduleCacheUpdate() {
    List<CustomerDiscordGuild> customerDiscordBroadcastConfigs = customerDiscordGuildCache.updateCustomers();
    log.info(
        "Received {} CustomerDiscordGuild objects while updating customer's cache",
        customerDiscordBroadcastConfigs.size()
    );
  }
}
