package io.enigmasolutions.twittermonitor.services.recognition;

import com.google.zxing.NotFoundException;
import io.enigmasolutions.broadcastmodels.Recognition;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public abstract class ImageRecognitionProcessor extends RecognitionProcessor {

    private final static String USER_AGENT = "Mozilla/5.0";

    @Override
    public Recognition processDataFromUrl(String url) throws Exception {
        InputStream inputStream = readInputStream(url);
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        return processDataFromBufferedImage(bufferedImage, url);
    }

    protected abstract Recognition processDataFromBufferedImage(BufferedImage bufferedImage, String url)
            throws IOException, NotFoundException;

    protected InputStream readInputStream(String url) throws IOException {
        URL connectionUrl = new URL(url);
        URLConnection urlConnection = connectionUrl.openConnection();
        urlConnection.addRequestProperty("User-Agent", USER_AGENT);

        return urlConnection.getInputStream();
    }
}
