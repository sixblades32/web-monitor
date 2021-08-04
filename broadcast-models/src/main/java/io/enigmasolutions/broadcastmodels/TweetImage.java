package io.enigmasolutions.broadcastmodels;

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
