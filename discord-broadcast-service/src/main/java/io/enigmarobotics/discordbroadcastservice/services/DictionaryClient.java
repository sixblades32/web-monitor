package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import io.enigmasolutions.dictionarymodels.CustomerDiscordBroadcast;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DictionaryClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String DICTIONARY_URL = "http://localhost:8080/customers/all/webhooks";

    public ResponseEntity<CustomerDiscordBroadcast[]> getWebhooks() {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<Message> request = new HttpEntity<>(headers);
        return restTemplate.exchange(DICTIONARY_URL, HttpMethod.GET, request, CustomerDiscordBroadcast[].class);
    }
}
