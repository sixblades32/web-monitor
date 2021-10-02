package io.enigmasolutions.twittermonitor.models.twitter.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import io.enigmasolutions.broadcastmodels.UserUpdateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

  private List<UserUpdateType> updateTypes = new LinkedList<>();

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

    return this.description.equals(user.getDescription())
            && this.location.equals(user.getLocation())
            && this.getUrl().equals(user.getUrl())
            && this.userImage.equals(user.getUserImage())
            && this.getScreenName().equals(user.getScreenName())
            && this.name.equals(user.getName());
  }

  public void setUpdateTypes(User user){
    if (!this.description.equals(user.getDescription())) {
      updateTypes.add(UserUpdateType.DESCRIPTION);
    }
    if (!this.location.equals(user.getLocation())) {
      updateTypes.add(UserUpdateType.LOCATION);
    }
    if (!this.getUrl().equals(user.getUrl())) {
      updateTypes.add(UserUpdateType.URL);
    }
    if (!this.userImage.equals(user.getUserImage())) {
      updateTypes.add(UserUpdateType.IMAGE);
    }
    if (!this.getScreenName().equals(user.getScreenName())) {
      updateTypes.add(UserUpdateType.SCREEN_NAME);
    }
    if (!this.name.equals(user.getName())) {
      updateTypes.add(UserUpdateType.NAME);
    }
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
