package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Core {
    public User user;
}
