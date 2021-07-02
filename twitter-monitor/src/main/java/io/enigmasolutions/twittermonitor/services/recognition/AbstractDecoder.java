package io.enigmasolutions.twittermonitor.services.recognition;

import com.google.zxing.NotFoundException;
import io.enigmasolutions.twittermonitor.exceptions.DecodeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public abstract class AbstractDecoder {

    private final static String USER_AGENT = "Mozilla/5.0";

    public String decodeDataFromUrl(String url) {
        try {
            InputStream inputStream = readInputStream(url);
            return processDataFromInputStream(inputStream);
        } catch (Exception e) {
            throw new DecodeException("Failed to decode data from url: " + url, e);
        }
    }

    protected abstract String processDataFromInputStream(InputStream inputStream) throws IOException, NotFoundException;

    protected InputStream readInputStream(String url) throws IOException {
        URL connectionUrl = new URL(url);
        URLConnection uc = connectionUrl.openConnection();
        uc.addRequestProperty("User-Agent", USER_AGENT);

        return uc.getInputStream();
    }
}
