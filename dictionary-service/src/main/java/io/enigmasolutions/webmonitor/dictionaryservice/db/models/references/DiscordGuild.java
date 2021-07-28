package io.enigmasolutions.webmonitor.dictionaryservice.db.models.references;

import lombok.Data;

import java.util.List;

@Data
public class DiscordGuild {
    private String guildId;
    private List<String> usersRoles;
    private List<String> moderatorsRoles;
}
