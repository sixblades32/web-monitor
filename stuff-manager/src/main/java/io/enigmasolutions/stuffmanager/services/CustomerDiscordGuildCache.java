package io.enigmasolutions.stuffmanager.services;

import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import io.enigmasolutions.stuffmanager.services.web.DictionaryClient;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerDiscordGuildCache {

    private final DictionaryClient dictionaryClient;

    public CustomerDiscordGuildCache(DictionaryClient dictionaryClient) {
        this.dictionaryClient = dictionaryClient;
    }


    @Cacheable(value = "customers")
    public List<CustomerDiscordGuild> getCustomers() {
        return dictionaryClient.getCustomerDiscordGuilds();
    }

    @CachePut(value = "customers")
    public List<CustomerDiscordGuild> updateCustomers() {
        return getCustomers();
    }
}
