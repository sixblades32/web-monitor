package io.enigmarobotics.discordbroadcastservice.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    private String name;
    private String url;
    @JsonProperty("icon_url")
    private String iconUrl;
}
