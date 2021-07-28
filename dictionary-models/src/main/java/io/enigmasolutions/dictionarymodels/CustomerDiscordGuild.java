package io.enigmasolutions.dictionarymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CustomerDiscordGuild {
    private String guildId;
    private List<String> usersRoles;
    private List<String> moderatorsRoles;
}
