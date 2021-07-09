package io.enigmasolutions.broadcastmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recognition {
    private RecognitionType type;
    private TweetType tweetType;
    private String source;
    private String result;
    private BriefTweet briefTweet;
    private BriefTweet nestedBriefTweet;
}
