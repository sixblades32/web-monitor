package io.enigmasolutions.broadcastmodels;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class Tweet {
    private String tweetType;
    private String text;
    private String userName;
    private String userId;
    private String userIcon;
    private String userUrl;
    private Tweet retweeted;
    private Tweet replied;
    private String tweetUrl;
    private String retweetsUrl;
    private String likesUrl;
    private String followsUrl;
    private String media;
    private String image;
    private List<String> images;
}
