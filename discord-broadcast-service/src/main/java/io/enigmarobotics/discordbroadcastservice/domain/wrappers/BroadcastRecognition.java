package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import lombok.Data;

@Data
public class BroadcastRecognition {

    private TweetType tweetType;
    private RecognitionType recognitionType;
    private String userName;
    private String nestedUserName;
    private String source;
    private String result;
}
