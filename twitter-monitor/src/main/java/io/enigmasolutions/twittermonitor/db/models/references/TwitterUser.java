package io.enigmasolutions.twittermonitor.db.models.references;

import lombok.Data;

@Data
public class TwitterUser {
    private String twitterId;
    private String screenName;
}
