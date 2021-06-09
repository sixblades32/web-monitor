package io.enigmarobotics.discordbroadcastservice.domain.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Message {

    private String content;
    private List<Embed> embeds;
}
