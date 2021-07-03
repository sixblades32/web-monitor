package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.twittermonitor.services.rest.PastebinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PastebinDecoder {
    private final PastebinClient pastebinClient;

    @Autowired
    public PastebinDecoder(PastebinClient pastebinClient) {
        this.pastebinClient = pastebinClient;
    }

    public String getPaste(String id){
        return pastebinClient.getResponseEntity(id).getBody();
    }
}
