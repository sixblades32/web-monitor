package io.enigmasolutions.twittermonitor.services.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleDocClient {

    private final String BASE_PATH = "https://docs.google.com/document/d/";
    private final String EXPORT_PATH = "/export";
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> getResponseEntity(String id){

        String url = UriComponentsBuilder.fromHttpUrl(BASE_PATH + id + EXPORT_PATH)
                .queryParam("format", "txt")
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(url)
                .headers(headers)
                .build();

        return restTemplate.exchange(requestEntity, String.class);
    }
}
