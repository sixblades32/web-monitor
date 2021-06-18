package io.enigmasolutions.twittermonitor.models.twitter.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Url {
    private String url;
    @JsonProperty("expanded_url")
    private String expandedUrl;
}
