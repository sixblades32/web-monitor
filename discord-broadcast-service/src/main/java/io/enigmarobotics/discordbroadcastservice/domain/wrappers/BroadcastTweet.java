package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import lombok.Data;

import java.util.List;

@Data
public class BroadcastTweet {

    private TweetType tweetType;
    private String text;
    private String userName;
    private String userId;
    private String userIcon;
    private String userUrl;
    private BroadcastTweet retweeted;
    private BroadcastTweet replied;
    private String image;
    private List<String> images;
    private String media;
    private String tweetUrl;
    private String retweetsUrl;
    private String likesUrl;
    private String followsUrl;
}
