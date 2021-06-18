package io.enigmasolutions.twittermonitor.models.twitter.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Media {
    private String type;
    @JsonProperty("media_url")
    private String mediaUrl;
    private String url;
    @JsonProperty("video_info")
    private VideoInfo videoInfo;
}
