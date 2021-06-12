package io.enigmasolutions.twittermonitor.models.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    @JsonProperty("screen_name")
    private String screenName;
    @JsonProperty("id_str")
    private String id;
    @JsonProperty("profile_image_url_https")
    private String userImage;
    private String userUrl;
    @JsonProperty("protected")
    private Boolean isProtected;

    public String getUserUrl() {
        return "https://twitter.com/" + screenName;
    }
}
