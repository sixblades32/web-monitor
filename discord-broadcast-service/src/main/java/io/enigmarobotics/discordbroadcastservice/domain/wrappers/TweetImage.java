package io.enigmarobotics.discordbroadcastservice.domain.wrappers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetImage {
    private String userId;
    private String image;
    private TweetType tweetType;
    private String userName;
    private String retweetedFrom;
}