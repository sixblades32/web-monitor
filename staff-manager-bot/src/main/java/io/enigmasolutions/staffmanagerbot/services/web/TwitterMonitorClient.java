package io.enigmasolutions.staffmanagerbot.services.web;

import io.enigmasolutions.broadcastmodels.FollowRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class TwitterMonitorClient {
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${twitter-monitor.url}")
  public String twitterMonitorUrl;

  private final String REQUEST_PATH = "/config/request";

  public void createFollowRequest(FollowRequest followRequest) {

    log.info(followRequest.toString());

    String fullUrl =
        UriComponentsBuilder.fromHttpUrl(twitterMonitorUrl + REQUEST_PATH).build().toUriString();

    log.info(fullUrl);

    HttpHeaders headers = new HttpHeaders();

    RequestEntity<FollowRequest> requestEntity =
        RequestEntity.post(fullUrl).headers(headers).body(followRequest);

    restTemplate.exchange(requestEntity, String.class);
  }
}
