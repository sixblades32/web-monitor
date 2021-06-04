package io.enigmarobotics.discordbroadcastservice.dto.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Embed {

    private String title;
    private String description;
    private int color;
    private List<Field> fields;

    public Author author;
    public Image image;
    public Footer footer;
}
