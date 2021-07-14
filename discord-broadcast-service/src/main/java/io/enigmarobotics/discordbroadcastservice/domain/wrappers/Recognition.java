package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import io.enigmasolutions.broadcastmodels.BriefTweet;
import lombok.Data;

@Data
public class Recognition {

    private RecognitionType type;
    private TweetType tweetType;
    private String source;
    private String result;
    private BriefTweet briefTweet;
    private BriefTweet nestedBriefTweet;
}
