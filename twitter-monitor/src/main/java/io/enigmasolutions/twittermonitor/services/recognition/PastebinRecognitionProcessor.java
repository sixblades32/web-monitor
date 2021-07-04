package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.twittermonitor.services.rest.PastebinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PastebinRecognitionProcessor extends PlainTextRecognitionProcessor {

    private static final String BASE_PATH = "https://pastebin.com/raw/";

    private final PastebinClient pastebinClient;

    @Autowired
    public PastebinRecognitionProcessor(PastebinClient pastebinClient) {
        super(BASE_PATH);
        this.pastebinClient = pastebinClient;
    }

    @Override
    protected Recognition processDataFromVerifiedUrl(String url) {
        String result = pastebinClient.getResponseEntity(url)
                .getBody();

        return Recognition.builder()
                .source(url)
                .result(result)
                .build();
    }
}
