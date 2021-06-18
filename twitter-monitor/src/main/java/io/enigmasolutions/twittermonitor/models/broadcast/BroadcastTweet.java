package io.enigmasolutions.twittermonitor.models.broadcast;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BroadcastTweet {
    private String tweetType;
    private String text;
    private String userName;
    private String userId;
    private String userIcon;
    private String userUrl;
    private BroadcastTweet retweeted;
    private BroadcastTweet replied;
    private String tweetUrl;
    private String retweetsUrl;
    private String likesUrl;
    private String followsUrl;
    private String media;
    private String image;
    private List<String> images;
}
