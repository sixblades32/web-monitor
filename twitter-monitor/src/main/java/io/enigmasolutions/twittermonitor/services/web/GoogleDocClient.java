package io.enigmasolutions.twittermonitor.services.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleDocClient {

  private static final String EXPORT_PATH = "/export";

  private final RestTemplate restTemplate = new RestTemplate();

  public ResponseEntity<String> getResponseEntity(String url) {
    String exportUrl = UriComponentsBuilder.fromHttpUrl(url + EXPORT_PATH)
        .queryParam("format", "txt")
        .build()
        .toUriString();

    HttpHeaders headers = new HttpHeaders();

    RequestEntity<Void> requestEntity = RequestEntity
        .get(exportUrl)
        .headers(headers)
        .build();

    return restTemplate.exchange(requestEntity, String.class);
  }
}
