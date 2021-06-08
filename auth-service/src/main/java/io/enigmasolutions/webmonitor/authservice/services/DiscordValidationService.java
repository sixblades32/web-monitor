package io.enigmasolutions.webmonitor.authservice.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Slf4j
@Service
public class DiscordValidationService {

    private final RestTemplate restTemplate;
    @Value("${discord.bot.token}")
    private String botToken;
    @Value("${discord.trusted-guild}")
    private String trustedGuild;

    public DiscordValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateMutualGuilds(String discordId) {
        boolean result = false;

        try {
            ResponseEntity<Void> responseEntity = getEmptyGuildMember(discordId);
            result = responseEntity.getStatusCode().is2xxSuccessful();
        } catch (RestClientResponseException responseException) {
            log.error(
                    "Failed to validate mutual servers for discordId: {}, {}",
                    discordId,
                    responseException.getResponseBodyAsString()
            );
        } catch (Exception exception) {
            log.error("Unknown exception", exception);
        }

        return result;
    }

    private ResponseEntity<Void> getEmptyGuildMember(String discordId) {
        String url = "https://discord.com/api/guilds/" + trustedGuild + "/members/" + discordId;

        HttpHeaders requestHeaders = new HttpHeaders();

        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        requestHeaders.add("User-Agent", "EnigmaSolutions");
        requestHeaders.add("Authorization", botToken);

        RequestEntity<Void> requestEntity = RequestEntity
                .get(url)
                .headers(requestHeaders)
                .build();

        return restTemplate.exchange(requestEntity, Void.class);
    }
}
