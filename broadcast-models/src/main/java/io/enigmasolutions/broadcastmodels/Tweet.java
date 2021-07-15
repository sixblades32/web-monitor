package io.enigmasolutions.broadcastmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tweet {
    private TweetType type;
    private String text;
    private TwitterUser user;
    private Tweet retweeted;
    private BriefTweet replied;
    private String tweetUrl;
    private String retweetsUrl;
    private String likesUrl;
    private String followsUrl;
    private String media;
    private List<String> images;
    private List<String> detectedUrls;
}
