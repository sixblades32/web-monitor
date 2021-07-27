package io.enigmasolutions.webmonitor.dictionaryservice.db.models.references;

import lombok.Data;

import java.util.List;

@Data
public class DiscordBroadcast {
    private List<String> commonWebhooks;
    private List<String> advancedWebhooks;
}

