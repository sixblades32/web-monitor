package io.enigmasolutions.twittermonitor.services.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PastebinClient {

    private final String API_PATH = "https://pastebin.com/raw/";
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> getResponseEntity(String id){

        String url = API_PATH + id;

        HttpHeaders headers = new HttpHeaders();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(url)
                .headers(headers)
                .build();

        return restTemplate.exchange(requestEntity, String.class);
    }


}
