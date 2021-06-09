package io.enigmarobotics.discordbroadcastservice.domain.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Field {
    private Boolean inline;
    private String name;
    private String value;
}
