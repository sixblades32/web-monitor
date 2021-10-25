package io.enigmasolutions.twittermonitor.services.web;

import io.enigmasolutions.broadcastmodels.FollowRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DiscordBroadcastClient {

  private static final String FOLLOW_REQUEST_PATH = "/postman/request";

  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${discord-broadcast-service.url}")
  public String discordBroadcastServiceUrl;

  public void createFollowRequest(FollowRequest followRequest) {
    String fullUrl =
        UriComponentsBuilder.fromHttpUrl(discordBroadcastServiceUrl + FOLLOW_REQUEST_PATH)
            .build()
            .toUriString();

    HttpHeaders headers = new HttpHeaders();

    RequestEntity<FollowRequest> requestEntity =
        RequestEntity.post(fullUrl).headers(headers).body(followRequest);

    restTemplate.exchange(requestEntity, String.class);
  }
}
