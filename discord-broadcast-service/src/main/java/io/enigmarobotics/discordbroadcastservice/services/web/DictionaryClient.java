package io.enigmarobotics.discordbroadcastservice.services.web;

import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcast;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class DictionaryClient {

    private final WebClient webClient;

    public DictionaryClient(@Value("${dictionary-service.url}") String dictionaryServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(dictionaryServiceUrl)
                .build();
    }

    public List<CustomerDiscordBroadcast> getWebhooks() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/customers/all/webhooks")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CustomerDiscordBroadcast>>() {
                })
                .block();
    }
}
