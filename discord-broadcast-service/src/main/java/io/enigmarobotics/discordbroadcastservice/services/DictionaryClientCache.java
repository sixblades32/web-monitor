package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DictionaryClientCache {

    private final DictionaryClient dictionaryClient;

    public DictionaryClientCache(DictionaryClient dictionaryClient) {
        this.dictionaryClient = dictionaryClient;
    }

    @Scheduled(fixedRate = 15000)
    private void scheduleCacheUpdate() {
        List<CustomerDiscordBroadcast> customerDiscordBroadcasts = updateWebhooks();
        log.info(
                "Received {} CustomerDiscordBroadcast objects while updating webhooks cache",
                customerDiscordBroadcasts.size()
        );
    }

    @Cacheable(value = "webhooks")
    public List<CustomerDiscordBroadcast> getWebhooks(){
        return dictionaryClient.getWebhooks();
    }

    @CachePut(value = "webhooks")
    public List<CustomerDiscordBroadcast> updateWebhooks(){
        return getWebhooks();
    }
}
