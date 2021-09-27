package io.enigmasolutions.twittermonitor.db.models.references;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Credentials {
    private String consumerKey;
    private String consumerSecret;
    private String token;
    private String tokenSecret;
}
