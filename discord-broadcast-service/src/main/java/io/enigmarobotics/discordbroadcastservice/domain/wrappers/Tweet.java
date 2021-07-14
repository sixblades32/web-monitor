package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import io.enigmasolutions.broadcastmodels.BriefTweet;
import io.enigmasolutions.broadcastmodels.TwitterUser;
import lombok.Data;

import java.util.List;

@Data
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
}
