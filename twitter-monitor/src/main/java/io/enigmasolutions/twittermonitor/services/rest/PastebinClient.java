package io.enigmasolutions.twittermonitor.services.rest;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PastebinClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> getResponseEntity(String url) {
        RequestEntity<Void> requestEntity = RequestEntity
                .get(url)
                .build();

        return restTemplate.exchange(requestEntity, String.class);
    }
}
