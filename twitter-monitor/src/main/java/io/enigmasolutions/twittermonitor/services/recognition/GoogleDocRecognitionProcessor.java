package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.RecognitionType;
import io.enigmasolutions.twittermonitor.services.rest.GoogleDocClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoogleDocRecognitionProcessor extends PlainTextRecognitionProcessor {

    private static final String BASE_PATH = "https://docs.google.com/document/d/";

    private final GoogleDocClient googleDocClient;

    @Autowired
    public GoogleDocRecognitionProcessor(GoogleDocClient googleDocClient) {
        super(BASE_PATH);
        this.googleDocClient = googleDocClient;
    }

    @Override
    protected Recognition processDataFromVerifiedUrl(String url) {
        String result = googleDocClient.getResponseEntity(url)
                .getBody();

        return Recognition.builder()
                .recognitionType(RecognitionType.GOOGLE_DOCS)
                .source(url)
                .result(result)
                .build();
    }
}
