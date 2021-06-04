package io.enigmarobotics.discordbroadcastservice.db.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "targets")
@Data
public class Target {
    @Id
    private String id;
    private String username;
    private String identifier;
}
