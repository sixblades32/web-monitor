package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Footer;

public enum RecognitionType {
    PASTEBIN {
        @Override
        public Footer generateTweetRecognitionFooter(BroadcastRecognition recognition) {

            return Footer.builder()
                    .text("Pastebin Recognition from TWEET  —  @" + recognition.getUserName())
                    .build();
        }
    },
    GOOGLE_DOCS {
        @Override
        public Footer generateTweetRecognitionFooter(BroadcastRecognition recognition) {
            return Footer.builder()
                    .text("GoogleDocs Recognition from TWEET  —  @" + recognition.getUserName())
                    .build();
        }
    },
    OCR {
        @Override
        public Footer generateTweetRecognitionFooter(BroadcastRecognition recognition) {
            return Footer.builder()
                    .text("OCR Recognition Result from TWEET  —  @" + recognition.getUserName())
                    .build();
        }
    },
    OR {
        @Override
        public Footer generateTweetRecognitionFooter(BroadcastRecognition recognition) {
            return Footer.builder()
                    .text("QR Recognition Result from TWEET  —  @" + recognition.getUserName())
                    .build();
        }
    };

    public abstract Footer generateTweetRecognitionFooter(BroadcastRecognition recognition);
}
