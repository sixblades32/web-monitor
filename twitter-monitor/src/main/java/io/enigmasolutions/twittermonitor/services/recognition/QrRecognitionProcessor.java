package io.enigmasolutions.twittermonitor.services.recognition;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.enigmasolutions.broadcastmodels.Recognition;
import io.enigmasolutions.broadcastmodels.RecognitionType;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class QrRecognitionProcessor extends ImageRecognitionProcessor {

    @Override
    protected Recognition processDataFromBufferedImage(BufferedImage bufferedImage, String url) throws NotFoundException, IOException {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);

        return Recognition.builder()
                .type(RecognitionType.OR)
                .source(url)
                .result(result.getText())
                .build();
    }
}