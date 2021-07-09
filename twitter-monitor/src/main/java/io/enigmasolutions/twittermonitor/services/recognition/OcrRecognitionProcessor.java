package io.enigmasolutions.twittermonitor.services.recognition;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectTextRequest;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.TextDetection;
import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.RecognitionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

@Service
public class OcrRecognitionProcessor extends ImageRecognitionProcessor {

    private final AmazonRekognition client;

    @Autowired
    public OcrRecognitionProcessor(AmazonRekognition client) {
        this.client = client;
    }

    @Override
    protected Recognition processDataFromBufferedImage(BufferedImage bufferedImage, String url) throws IOException {
        StringBuilder result = new StringBuilder();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);

        byte[] byteArray = baos.toByteArray();
        DetectTextRequest request = new DetectTextRequest()
                .withImage(new Image().withBytes(ByteBuffer.wrap(byteArray)));

        List<TextDetection> textDetections = client.detectText(request).getTextDetections();

        for (TextDetection textDetection : textDetections) {
            if (textDetection.getType().equals("LINE")) {
                result.append(textDetection.getDetectedText());
                result.append("\n");
            }
        }

        return Recognition.builder()
                .type(RecognitionType.OCR)
                .source(url)
                .result(result.toString())
                .build();
    }
}