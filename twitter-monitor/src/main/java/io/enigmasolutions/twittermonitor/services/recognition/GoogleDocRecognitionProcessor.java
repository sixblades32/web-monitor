package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.RecognitionType;
import io.enigmasolutions.twittermonitor.services.rest.GoogleDocClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    protected Recognition processDataFromVerifiedUrl(String sourceUrl) {

        String url = BASE_PATH + retrieveDocumentId(sourceUrl);

        String result = googleDocClient.getResponseEntity(url)
                .getBody();

        return Recognition.builder()
                .type(RecognitionType.GOOGLE_DOC)
                .source(url)
                .result(result)
                .build();
    }

    private String retrieveDocumentId(String sourceUrl){

        System.out.println(sourceUrl);
        String regex = "[-\\w]{25,}";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sourceUrl);

        if(matcher.find()){
            return matcher.group(0);
        }

        return null;
    }
}
