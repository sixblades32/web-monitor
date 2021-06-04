package io.enigmarobotics.discordbroadcastservice.dto.wrappers;

import lombok.Data;

@Data
public class TweetImage {
    private String userId;
    private String image;
    private TweetType tweetType;
    private String userName;
    private String retweetedFrom;
}
