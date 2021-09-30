package io.enigmasolutions.twittermonitor.models.twitter.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Media {

  private String type;
  @JsonProperty("media_url_https")
  private String mediaUrl;
  private String url;
  @JsonProperty("video_info")
  private VideoInfo videoInfo;
  @JsonProperty("id_str")
  private String sourceUserId;
}
