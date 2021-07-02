package io.enigmasolutions.twittermonitor.services.recognition;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.TextDetection;
import com.amazonaws.util.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

@Service
public class OcrDecoder extends AbstractDecoder {

    private final AmazonRekognition client;

    public OcrDecoder(AmazonRekognition client) {
        this.client = client;
    }

    @Override
    protected String processDataFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();

        byte[] b = IOUtils.toByteArray(inputStream);
        DetectTextRequest request = new DetectTextRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(b)));

        List<TextDetection> textDetections = client.detectText(request).getTextDetections();

        for (TextDetection textDetection : textDetections) {
            if (textDetection.getType().equals("LINE")) {
                result.append(textDetection.getDetectedText());
                result.append("\n");
            }
        }

        return result.toString();
    }
}