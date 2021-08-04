package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcast;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DictionaryClient {

    private final WebClient webClient;

    public DictionaryClient(@Value("${dictionary-service.url}") String dictionaryServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(dictionaryServiceUrl)
                .build();
    }

    public CustomerDiscordBroadcast[] getWebhooks() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/customers/all/webhooks")
                        .build())
                .retrieve()
                .bodyToMono(CustomerDiscordBroadcast[].class)
                .block();
    }
}
