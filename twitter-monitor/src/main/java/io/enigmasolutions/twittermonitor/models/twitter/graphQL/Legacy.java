package io.enigmasolutions.twittermonitor.models.twitter.graphQL;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.enigmasolutions.broadcastmodels.TweetType;
import io.enigmasolutions.twittermonitor.models.twitter.base.Entity;
import io.enigmasolutions.twittermonitor.models.twitter.base.ExtendedEntity;
import io.enigmasolutions.twittermonitor.models.twitter.base.QuotedStatusPermalink;
import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import lombok.Builder;
import lombok.Data;

import static io.enigmasolutions.broadcastmodels.TweetType.*;

@Data
@Builder
public class Legacy {
    private final String TWITTER_URL = "https://twitter.com/";

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
    @JsonProperty("retweeted_status")
    private GraphQLTweet retweetedStatus;
    @JsonProperty("quoted_status")
    private GraphQLTweet quotedStatus;
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

        if (retweetedStatus != null || quotedStatus != null) {
            tweetType = RETWEET;
        } else if (inReplyToStatusId != null) {
            tweetType = REPLY;
        } else {
            // TODO: добавить какой-нибудь рантайм экспешн --> package exceptions UnsupportedTweetTypeException, чтобы дальше это дело не разлетолсь по консумерам
        }

        return tweetType;
    }

    public GraphQLTweet getRepliedStatus() {
        if (inReplyToStatusId != null) {
            User user = User.builder()
                    .id(inReplyToUserId)
                    .screenName(inReplyToScreenName)
                    .userUrl(TWITTER_URL + inReplyToScreenName)
                    .build();

            Core core = Core.builder()
                    .user(user)
                    .build();

            GraphQLTweet replied = GraphQLTweet.builder()
                    .core(core)
                    .tweetUrl(TWITTER_URL + inReplyToScreenName + "/status/" + inReplyToStatusId)
                    .build();

            return replied;
        }
        return null;
    }

    public String getText() {
        if (retweetedStatus != null) {
            text = "";
        } else if (inReplyToStatusId != null) {
            text = text.replace("@" + inReplyToScreenName + " ", "");
        } else if (quotedStatus != null) {
            text = text.replace(quotedStatusPermalink.getUrl(), "");
        }

        return text;
    }
}
