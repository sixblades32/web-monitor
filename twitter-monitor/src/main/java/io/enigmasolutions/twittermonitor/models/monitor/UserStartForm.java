package io.enigmasolutions.twittermonitor.models.monitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserStartForm {
    @JsonProperty("username")
    private String userName;
}
