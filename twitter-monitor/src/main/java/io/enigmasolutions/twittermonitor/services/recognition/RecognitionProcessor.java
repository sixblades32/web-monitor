package io.enigmasolutions.twittermonitor.services.recognition;

import io.enigmasolutions.broadcastmodels.Recognition;

public interface RecognitionProcessor {

    Recognition processDataFromUrl(String url) throws Exception;
}
