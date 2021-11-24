package io.enigmasolutions.staffmanagerbot.services.web;

import io.enigmasolutions.broadcastmodels.FollowRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class TwitterMonitorClient {
  private final RestTemplate restTemplate = new RestTemplate();
  @Value("${twitter-monitor.user.name}")
  private String authName;
  @Value("${twitter-monitor.user.name}")
  private String authPassword;
  @Value("${twitter-monitor.url}")
  private String twitterMonitorUrl;

  private final String REQUEST_PATH = "/config/request";

  public void createFollowRequest(FollowRequest followRequest) {
    String fullUrl =
        UriComponentsBuilder.fromHttpUrl(twitterMonitorUrl + REQUEST_PATH).build().toUriString();

    restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(authName, authPassword));

    HttpHeaders headers = new HttpHeaders();

    RequestEntity<FollowRequest> requestEntity =
        RequestEntity.post(fullUrl).headers(headers).body(followRequest);

    restTemplate.exchange(requestEntity, String.class);
  }
}
