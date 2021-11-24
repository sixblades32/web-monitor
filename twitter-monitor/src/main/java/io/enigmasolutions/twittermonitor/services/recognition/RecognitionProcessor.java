package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.broadcastmodels.Recognition;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RecognitionProcessor {

    private final static String URL_REGEXP =
            "\\b((?:https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:, .;]*[-a-zA-Z0-9+&@#/%=~_|])";

    public Recognition processUrl(String url) throws Exception {
        Recognition recognition = processDataFromUrl(url);
        recognition.setDetectedUrls(detectUrlsFromRecognitionResult(recognition.getResult()));

        return recognition;
    }

    protected abstract Recognition processDataFromUrl(String url) throws Exception;

    private List<String> detectUrlsFromRecognitionResult(String text) {
        List<String> urls = new ArrayList<>();

        Pattern pattern = Pattern.compile(URL_REGEXP, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            urls.add(text.substring(matcher.start(0),
                    matcher.end(0)));
        }

        return urls;
    }
}
