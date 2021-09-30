package io.enigmasolutions.twittermonitor.models.twitter.v2;

import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import java.util.LinkedHashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlobalObjects {

  private final String TWITTER_URL = "https://twitter.com/";

  private LinkedHashMap<String, Tweet> tweets;
  private LinkedHashMap<String, User> users;
}
