package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import lombok.Data;

import java.util.List;

@Data
public class Tweet {

    private TweetType tweetType;
    private String text;
    private String userName;
    private String userId;
    private String userIcon;
    private String userUrl;
    private Tweet retweeted;
    private Tweet replied;
    //    private String image;
    private String tweetUrl;
    private String retweetsUrl;
    private String likesUrl;
    private String followsUrl;
    private List<String> tweetImageUrlList;
}
