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
public class UserLegacy {

  @JsonProperty("screen_name")
  private String screenName;
  private String name;
  @JsonProperty("profile_image_url_https")
  private String userImage;
  private String userUrl;
  @JsonProperty("protected")
  private Boolean isProtected;

  public String getUserUrl() {
    return "https://twitter.com/" + screenName;
  }
}
