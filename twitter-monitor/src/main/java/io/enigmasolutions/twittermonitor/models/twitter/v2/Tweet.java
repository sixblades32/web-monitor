package io.enigmasolutions.twittermonitor.models.twitter.v2;

import static io.enigmasolutions.broadcastmodels.TweetType.REPLY;
import static io.enigmasolutions.broadcastmodels.TweetType.RETWEET;
import static io.enigmasolutions.broadcastmodels.TweetType.TWEET;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.enigmasolutions.broadcastmodels.TweetType;
import io.enigmasolutions.twittermonitor.models.twitter.base.Entity;
import io.enigmasolutions.twittermonitor.models.twitter.base.ExtendedEntity;
import io.enigmasolutions.twittermonitor.models.twitter.base.QuotedStatusPermalink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tweet {

  private TweetType type;
  @JsonProperty("created_at")
  private String createdAt;
  @JsonProperty("id_str")
  private String tweetId;
  @JsonProperty("full_text")
  private String text;
  private Entity entities;
  @JsonProperty("extended_entities")
  private ExtendedEntity extendedEntities;
  @JsonProperty("in_reply_to_status_id_str")
  private String inReplyToStatusId;
  @JsonProperty("in_reply_to_user_id_str")
  private String inReplyToUserId;
  @JsonProperty("in_reply_to_screen_name")
  private String inReplyToScreenName;
  @JsonProperty("user_id_str")
  private String userId;
  @JsonProperty("retweeted_status_id_str")
  private String retweetedStatusId;
  @JsonProperty("is_quote_status")
  private boolean isQuoteStatus;
  @JsonProperty("quoted_status_permalink")
  private QuotedStatusPermalink quotedStatusPermalink;

  public TweetType getType() {
    type = TWEET;

    if (retweetedStatusId != null || isQuoteStatus) {
      type = RETWEET;
    } else if (inReplyToStatusId != null) {
      type = REPLY;
    }

    return type;
  }

  public String getText() {
    if (retweetedStatusId != null) {
      text = "";
    }

    return text;
  }
}
