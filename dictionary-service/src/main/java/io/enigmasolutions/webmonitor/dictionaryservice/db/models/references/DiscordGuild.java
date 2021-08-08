package io.enigmasolutions.webmonitor.dictionaryservice.db.models.references;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscordGuild {
    private String guildId;
    private List<String> usersRoles;
    private List<String> moderatorsRoles;
}
