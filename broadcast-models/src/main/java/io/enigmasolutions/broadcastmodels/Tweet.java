package io.enigmasolutions.broadcastmodels;

import java.util.List;
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
  private String text;
  private TwitterUser user;
  private Tweet retweeted;
  private BriefTweet replied;
  private List<Media> media;
  private List<String> detectedUrls;
  private String tweetUrl;
  private String retweetsUrl;
  private String likesUrl;
  private String followsUrl;
}
