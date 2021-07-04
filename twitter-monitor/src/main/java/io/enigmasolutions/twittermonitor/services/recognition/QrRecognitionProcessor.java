package io.enigmasolutions.twittermonitor.services.recognition;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class QrRecognitionProcessor extends ImageRecognitionProcessor {

    @Override
    protected String processDataFromBufferedImage(BufferedImage bufferedImage) throws NotFoundException, IOException {
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);

        return result.getText();
    }
}