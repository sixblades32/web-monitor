package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GraphQLTweet {
    private final String TWITTER_URL = "https://twitter.com/";

    @JsonProperty("rest_id")
    private String restId;
    private Core core;
    private TweetLegacy legacy;
    @JsonProperty("quoted_status_result")
    private RetweetedStatusResult quotedStatus;

    private String tweetUrl;
    private String retweetsUrl;
    private String likesUrl;
    private String followsUrl;

    public String getTweetUrl() {
        tweetUrl = TWITTER_URL + core.getUser().getLegacy().getScreenName() + "/status/" + legacy.getTweetId();
        return tweetUrl;
    }

    public String getRetweetsUrl() {
        retweetsUrl = TWITTER_URL + core.getUser().getLegacy().getScreenName() + "/status/" + legacy.getTweetId() +
                "/retweets";
        return retweetsUrl;
    }

    public String getFollowsUrl() {
        followsUrl = TWITTER_URL + core.getUser().getLegacy().getScreenName() + "/followers";
        return followsUrl;
    }

    public String getLikesUrl() {
        likesUrl = TWITTER_URL + core.getUser().getLegacy().getScreenName() + "/status/" + legacy.getTweetId() +
                "/likes";
        return likesUrl;
    }
}
