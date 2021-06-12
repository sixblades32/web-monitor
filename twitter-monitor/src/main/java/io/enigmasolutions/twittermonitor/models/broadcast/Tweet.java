package io.enigmasolutions.twittermonitor.models.broadcast;

import lombok.Data;

@Data
public class Tweet {

    private String tweetType;
    private String text;
    private String userName;
    private String userId;
    private String userIcon;
    private String userUrl;
    private Tweet retweeted;
    private Tweet replied;
    private String image;
    private String tweetUrl;
    private String retweetsUrl;
    private String likesUrl;
    private String followsUrl;
}
