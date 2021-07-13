package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import io.enigmarobotics.discordbroadcastservice.configuration.DiscordEmbedColorConfig;
import io.enigmarobotics.discordbroadcastservice.domain.models.Footer;

public enum RecognitionType {
    PASTEBIN {
        @Override
        public String toString() {
            return "Pastebin";
        }
    },
    GOOGLE_DOC {
        @Override
        public String toString() {
            return "GoogleDoc";
        }
    },
    OCR,
    QR;

    public String getTitle() {
        return this + " Recognition Result: ";
    }

    ;

    public Footer generateTweetRecognitionFooter(BroadcastRecognition recognition) {
        return Footer.builder()
                .text(this + " Recognition Result from TWEET  —  @" + recognition.getUserName())
                .build();
    }

    ;

    public Footer generateRetweetRecognitionFooter(BroadcastRecognition recognition) {
        return Footer.builder()
                .text(this + " Recognition from RETWEET  —  @" + recognition.getUserName() + " --> " + recognition.getNestedUserName())
                .build();
    }

    ;

    public Footer generateReplyRecognitionFooter(BroadcastRecognition recognition) {
        return Footer.builder()
                .text(this + " Recognition from REPLY  —  @" + recognition.getUserName() + " --> " + recognition.getNestedUserName())
                .build();
    }

    ;
}
