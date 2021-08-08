package io.enigmarobotics.discordbroadcastservice.services.web;

import io.enigmarobotics.discordbroadcastservice.domain.models.Message;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DiscordClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> sendEmbed(String url, Message message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Message> request = new HttpEntity<>(message, headers);
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
