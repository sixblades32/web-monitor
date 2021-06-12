package io.enigmasolutions.twittermonitor.models.broadcast;

import lombok.Data;

@Data
public class TweetImage {
    private String userId;
    private String image;
    private String tweetType;
    private String userName;
    private String retweetedFrom;
}
