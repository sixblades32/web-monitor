package io.enigmasolutions.webmonitor.dictionaryservice.db.models.documents;

import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.DiscordBroadcast;
import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.DiscordGuild;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Data
public class Customer {
    @Id
    private String id;
    private String name;
    private String comment;
    private DiscordBroadcast discordBroadcast;
    private DiscordGuild discordGuild;
}
