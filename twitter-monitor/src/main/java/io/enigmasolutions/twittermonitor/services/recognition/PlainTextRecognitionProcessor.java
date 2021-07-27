package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.broadcastmodels.Recognition;

public abstract class PlainTextRecognitionProcessor extends RecognitionProcessor {

    private final String basePath;

    protected PlainTextRecognitionProcessor(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public Recognition processDataFromUrl(String url) {
        if (!validateUrl(url))
            throw new IllegalStateException("Url is not valid, expected: " + basePath + " received: " + url);

        return processDataFromVerifiedUrl(url);
    }

    protected abstract Recognition processDataFromVerifiedUrl(String url);

    private boolean validateUrl(String url) {
        return url.startsWith(basePath);
    }
}
