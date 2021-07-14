package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.RecognitionType;
import io.enigmasolutions.twittermonitor.services.rest.PastebinClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PastebinRecognitionProcessor extends PlainTextRecognitionProcessor {

    private static final String BASE_PATH = "https://pastebin.com";
    private static final String RAW_PATH = "/raw";

    private final PastebinClient pastebinClient;

    @Autowired
    public PastebinRecognitionProcessor(PastebinClient pastebinClient) {
        super(BASE_PATH);
        this.pastebinClient = pastebinClient;
    }

    @Override
    protected Recognition processDataFromVerifiedUrl(String sourceUrl) {

        String url = BASE_PATH + RAW_PATH + "/" + retrievePasteId(sourceUrl);

        String result = pastebinClient.getResponseEntity(url)
                .getBody();

        return Recognition.builder()
                .type(RecognitionType.PASTEBIN)
                .source(url)
                .result(result)
                .build();
    }

    private String retrievePasteId(String sourceUrl) {

        String regex = "[\\w]{8,}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sourceUrl);

        if (matcher.find()) {
            return matcher.group(0);
        }

        return null;
    }
}
