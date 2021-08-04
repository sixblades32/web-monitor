package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcast;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DictionaryClientService {

    private final DictionaryClient dictionaryClient;

    public DictionaryClientService(DictionaryClient dictionaryClient) {
        this.dictionaryClient = dictionaryClient;
    }

    @Scheduled(fixedRate = 15000)
    private void scheduleCacheUpdate(){
        updateWebhooks();
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
