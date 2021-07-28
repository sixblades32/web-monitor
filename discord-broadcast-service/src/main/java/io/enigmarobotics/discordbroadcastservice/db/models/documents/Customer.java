package io.enigmarobotics.discordbroadcastservice.db.models.documents;

import io.enigmarobotics.discordbroadcastservice.db.models.references.DiscordBroadcast;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Data
public class Customer {
    @Id
    private String id;
    private String customerId;
    private DiscordBroadcast discordBroadcast;
}