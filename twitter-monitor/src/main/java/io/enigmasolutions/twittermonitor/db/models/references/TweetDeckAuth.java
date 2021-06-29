package io.enigmasolutions.twittermonitor.db.models.references;

import lombok.Data;

@Data
public class TweetDeckAuth {
    private String bearer;
    private String authToken;
    private String csrfToken;
}
