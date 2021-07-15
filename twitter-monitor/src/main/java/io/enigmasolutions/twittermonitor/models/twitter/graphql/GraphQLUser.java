package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GraphQLUser {

    @JsonProperty("rest_id")
    private String restId;
    private UserLegacy legacy;
}
