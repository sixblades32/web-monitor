package io.enigmasolutions.twittermonitor.db.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class TweetDeckAuth {
    private String bearer;
    @Field("auth_token")
    private String authToken;
    @Field("csrf_token")
    private String csrfToken;
}
