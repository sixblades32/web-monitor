package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TweetImage {
    private String userId;
    private String image;
    private TweetType tweetType;
    private String userName;
    private String retweetedFrom;
}
