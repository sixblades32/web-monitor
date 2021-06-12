package io.enigmasolutions.twittermonitor.db.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Credentials {
    @Field("consumer_key")
    private String consumerKey;
    @Field("consumer_secret")
    private String consumerSecret;
    private String token;
    @Field("token_secret")
    private String tokenSecret;
}
