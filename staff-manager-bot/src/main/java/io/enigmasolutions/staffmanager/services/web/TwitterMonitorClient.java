package io.enigmasolutions.staffmanager.services.web;

import io.enigmasolutions.broadcastmodels.FollowRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TwitterMonitorClient {
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${twitter-monitor.url}")
  public String twitterMonitorUrl;

  private final String USER_PATH = "/config/request";

  public void createFollowRequest(FollowRequest followRequest) {
    String fullUrl =
        UriComponentsBuilder.fromHttpUrl(twitterMonitorUrl + USER_PATH).build().toUriString();

    HttpHeaders headers = new HttpHeaders();

    RequestEntity<FollowRequest> requestEntity =
        RequestEntity.post(fullUrl).headers(headers).body(followRequest);

    restTemplate.exchange(requestEntity, String.class);
  }
}
