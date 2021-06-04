package io.enigmarobotics.discordbroadcastservice.services;

import io.enigmarobotics.discordbroadcastservice.dto.models.*;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.Alert;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.Tweet;
import io.enigmarobotics.discordbroadcastservice.dto.wrappers.TweetImage;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
