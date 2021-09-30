package io.enigmasolutions.twittermonitor.models.twitter.base;

import static io.enigmasolutions.broadcastmodels.TweetType.REPLY;
import static io.enigmasolutions.broadcastmodels.TweetType.RETWEET;
import static io.enigmasolutions.broadcastmodels.TweetType.TWEET;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.enigmasolutions.broadcastmodels.TweetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TweetResponse {

  private final String TWITTER_URL = "https://twitter.com/";

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
  private User user;
  @JsonProperty("retweeted_status")
  private TweetResponse retweetedStatus;
  @JsonProperty("quoted_status")
  private TweetResponse quotedStatus;
  private TweetResponse repliedStatus;
  @JsonProperty("quoted_status_permalink")
  private QuotedStatusPermalink quotedStatusPermalink;
  @JsonProperty("in_reply_to_status_id_str")
  private String inReplyToStatusId;
  @JsonProperty("in_reply_to_user_id_str")
  private String inReplyToUserId;
  @JsonProperty("in_reply_to_screen_name")
  private String inReplyToScreenName;

  private String tweetUrl;
  private String retweetsUrl;
  private String likesUrl;
  private String followsUrl;

  public TweetType getType() {
    type = TWEET;

    if (retweetedStatus != null || quotedStatus != null) {
      type = RETWEET;
    } else if (inReplyToStatusId != null) {
      type = REPLY;
    }

    return type;
  }

  public TweetResponse getRepliedStatus() {
    if (inReplyToStatusId == null) {
      return null;
    }

    User user = User.builder()
        .id(inReplyToUserId)
        .screenName(inReplyToScreenName)
        .userUrl(TWITTER_URL + inReplyToScreenName)
        .build();

    return TweetResponse.builder()
        .user(user)
        .tweetId(inReplyToStatusId)
        .build();
  }

  public String getTweetUrl() {
    tweetUrl = TWITTER_URL + user.getScreenName() + "/status/" + tweetId;
    return tweetUrl;
  }

  public String getRetweetsUrl() {
    retweetsUrl = TWITTER_URL + user.getScreenName() + "/status/" + tweetId + "/retweets";
    return retweetsUrl;
  }

  public String getFollowsUrl() {
    followsUrl = TWITTER_URL + user.getScreenName() + "/followers";
    return followsUrl;
  }

  public String getLikesUrl() {
    likesUrl = TWITTER_URL + user.getScreenName() + "/status/" + tweetId + "/likes";
    return likesUrl;
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
