package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.twittermonitor.services.rest.GoogleDocClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleDocDecoder {
    private final GoogleDocClient googleDocClient;

    @Autowired
    public GoogleDocDecoder(GoogleDocClient googleDocClient) {
        this.googleDocClient = googleDocClient;
    }

    public String getGoogleDocText(String id){
       return googleDocClient.getResponseEntity(id).getBody();
    }
}
