package io.enigmasolutions.twittermonitor.models.twitter.graphQL;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GraphQLTweet {
    private final String TWITTER_URL = "https://twitter.com/";

    @JsonProperty("rest_id")
    private String restId;
    private Core core;
    private Legacy legacy;

    private String tweetUrl;
    private String retweetsUrl;
    private String likesUrl;
    private String followsUrl;

    public String getTweetUrl() {
        tweetUrl = TWITTER_URL + core.getUser().getScreenName() + "/status/" + legacy.getTweetId();
        return tweetUrl;
    }

    public String getRetweetsUrl() {
        retweetsUrl = TWITTER_URL + core.getUser().getScreenName() + "/status/" + legacy.getTweetId() + "/retweets";
        return retweetsUrl;
    }

    public String getFollowsUrl() {
        followsUrl = TWITTER_URL + core.getUser().getScreenName() + "/followers";
        return followsUrl;
    }

    public String getLikesUrl() {
        likesUrl = TWITTER_URL + core.getUser().getScreenName() + "/status/" + legacy.getTweetId() + "/likes";
        return likesUrl;
    }
}
