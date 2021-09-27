package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.services.web.DictionaryClient;
import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcastConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DictionaryClientCache {

    private final DictionaryClient dictionaryClient;

    public DictionaryClientCache(DictionaryClient dictionaryClient) {
        this.dictionaryClient = dictionaryClient;
    }


    @Cacheable(value = "customers")
    public List<CustomerDiscordBroadcastConfig> getCustomersConfigs() {
        return dictionaryClient.getCustomerConfig();
    }

    @CachePut(value = "customers")
    public List<CustomerDiscordBroadcastConfig> updateCustomers() {
        return getCustomersConfigs();
    }
}
