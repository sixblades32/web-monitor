package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import io.enigmasolutions.twittermonitor.models.twitter.base.User;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Core {
    public User user;
}
