package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmasolutions.dictionarymodels.CustomerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class DictionaryClientCacheScheduler {

    private final DictionaryClientCache dictionaryClientCache;

    public DictionaryClientCacheScheduler(DictionaryClientCache dictionaryClientCache) {
        this.dictionaryClientCache = dictionaryClientCache;
    }

    @Scheduled(fixedRate = 15000)
    public void scheduleCacheUpdate() {
        List<CustomerConfig> customerConfigs = dictionaryClientCache.updateCustomers();
        log.info(
                "Received {} CustomerDiscordBroadcast objects while updating webhooks cache",
                customerConfigs.size()
        );
    }
}
