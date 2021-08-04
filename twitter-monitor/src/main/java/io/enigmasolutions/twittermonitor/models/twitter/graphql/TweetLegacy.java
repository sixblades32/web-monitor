package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.enigmasolutions.broadcastmodels.TweetType;
import io.enigmasolutions.twittermonitor.models.twitter.base.Entity;
import io.enigmasolutions.twittermonitor.models.twitter.base.ExtendedEntity;
import io.enigmasolutions.twittermonitor.models.twitter.base.QuotedStatusPermalink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static io.enigmasolutions.broadcastmodels.TweetType.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetLegacy {

    private final static String TWITTER_URL = "https://twitter.com/";

    private TweetType tweetType;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("conversation_id_str")
    private String tweetId;
    @JsonProperty("full_text")
    private String text;
    private Entity entities;
    @JsonProperty("extended_entities")
    private ExtendedEntity extendedEntities;
    @JsonProperty("retweeted_status_result")
    private RetweetedStatusResult retweetedStatus;
    private GraphQLTweet repliedStatus;
    @JsonProperty("quoted_status_permalink")
    private QuotedStatusPermalink quotedStatusPermalink;
    @JsonProperty("in_reply_to_status_id_str")
    private String inReplyToStatusId;
    @JsonProperty("in_reply_to_user_id_str")
    private String inReplyToUserId;
    @JsonProperty("in_reply_to_screen_name")
    private String inReplyToScreenName;

    public TweetType getTweetType() {
        tweetType = TWEET;

        if (retweetedStatus != null || quotedStatusPermalink != null) {
            tweetType = RETWEET;
        } else if (inReplyToStatusId != null) {
            tweetType = REPLY;
        }

        return tweetType;
    }

    public GraphQLTweet getRepliedStatus() {
        if (inReplyToStatusId == null) return null;

        TweetLegacy tweetLegacy = TweetLegacy.builder()
                .tweetId(inReplyToStatusId)
                .build();

        UserLegacy userLegacy = UserLegacy.builder()
                .screenName(inReplyToScreenName)
                .userUrl(TWITTER_URL + inReplyToScreenName)
                .build();

        GraphQLUser user = GraphQLUser.builder()
                .restId(inReplyToUserId)
                .legacy(userLegacy)
                .build();

        Core core = Core.builder()
                .user(user)
                .build();

        return GraphQLTweet.builder()
                .legacy(tweetLegacy)
                .core(core)
                .tweetUrl(TWITTER_URL + inReplyToScreenName + "/status/" + inReplyToStatusId)
                .build();
    }


    public String getText() {
        if (retweetedStatus != null) {
            text = "";
        } else if (inReplyToStatusId != null) {
            text = text.replace("@" + inReplyToScreenName + " ", "");
        } else if (quotedStatusPermalink != null) {
            text = text.replace(quotedStatusPermalink.getUrl(), "");
        }

        return text;
    }
}
