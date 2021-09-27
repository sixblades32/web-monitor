package io.enigmasolutions.webmonitor.dictionaryservice.db.models.documents;

import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.DiscordBroadcast;
import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.DiscordGuild;
import io.enigmasolutions.webmonitor.dictionaryservice.db.models.references.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String name;
    private String comment;
    private DiscordBroadcast discordBroadcast;
    private DiscordGuild discordGuild;
    private Theme theme;
}
