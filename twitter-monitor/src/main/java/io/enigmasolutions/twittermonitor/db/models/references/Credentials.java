package io.enigmasolutions.twittermonitor.db.models.references;

import lombok.Data;

@Data
public class Credentials {
    private String consumerKey;
    private String consumerSecret;
    private String token;
    private String tokenSecret;
}
