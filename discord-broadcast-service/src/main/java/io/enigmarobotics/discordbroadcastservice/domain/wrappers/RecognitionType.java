package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

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

    public Footer generateTweetRecognitionFooter(Recognition recognition) {
        return Footer.builder()
                .text(this + " Recognition Result from TWEET  —  @" + recognition.getBriefTweet().getUser().getLogin())
                .build();
    }

    public Footer generateRetweetRecognitionFooter(Recognition recognition) {
        return Footer.builder()
                .text(this + " Recognition from RETWEET  —  @" + recognition.getBriefTweet().getUser().getLogin() +
                        " --> " + recognition.getNestedBriefTweet().getUser().getLogin())
                .build();
    }

    public Footer generateReplyRecognitionFooter(Recognition recognition) {
        return Footer.builder()
                .text(this + " Recognition from REPLY  —  @" + recognition.getBriefTweet().getUser().getLogin() +
                        " --> " + recognition.getNestedBriefTweet().getUser().getLogin())
                .build();
    }

}
