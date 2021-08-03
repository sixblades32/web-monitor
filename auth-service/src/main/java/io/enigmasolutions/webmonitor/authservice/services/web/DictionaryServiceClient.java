package io.enigmasolutions.webmonitor.authservice.services.web;

import io.enigmasolutions.dictionarymodels.CustomerDiscordGuild;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DictionaryServiceClient {

    private final WebClient webClient;

    public DictionaryServiceClient(
            @Value("${dictionary-service.url}") String dictionaryServiceUrl
    ) {
        this.webClient = WebClient.builder()
                .baseUrl(dictionaryServiceUrl)
                .build();
    }

    public CustomerDiscordGuild retrieveCustomerDiscordGuild(String customerId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/customers/{id}/guild")
                        .build(customerId))
                .retrieve()
                .bodyToMono(CustomerDiscordGuild.class)
                .block();
    }
}
