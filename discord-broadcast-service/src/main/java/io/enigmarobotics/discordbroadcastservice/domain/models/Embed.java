package io.enigmarobotics.discordbroadcastservice.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Embed {

    public Author author;
    public Image image;
    public Footer footer;
    private String title;
    private String description;
    private int color;
    private List<Field> fields;
}
