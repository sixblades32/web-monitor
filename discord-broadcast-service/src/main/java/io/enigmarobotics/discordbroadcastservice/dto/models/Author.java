package io.enigmarobotics.discordbroadcastservice.dto.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {

    private final String name;
    private final String url;
    @JsonProperty("icon_url")
    private final String iconUrl;
}
