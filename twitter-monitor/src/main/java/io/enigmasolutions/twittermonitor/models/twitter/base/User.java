package io.enigmasolutions.twittermonitor.models.twitter.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @JsonProperty("id_str")
  private String id;
  @JsonProperty("screen_name")
  private String screenName;
  private String name;
  @JsonProperty("profile_image_url_https")
  private String userImage;
  private String location;
  private String description;
  private String url;
  private String userUrl;
  @JsonProperty("protected")
  private Boolean isProtected;

  public Boolean isInfoEqual(User user) {
    return this.description.equals(user.getDescription()) &&
        this.location.equals(user.getLocation()) &&
        this.getUrl().equals(user.getUrl());
  }

  public String getUserUrl() {
    return "https://twitter.com/" + screenName;
  }

  public String getScreenName() {
    return screenName.toLowerCase(Locale.ROOT);
  }

  public String getUrl() {
    if (url == null) {
      return "";
    }

    return url;
  }
}
