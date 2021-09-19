package io.enigmasolutions.dictionarymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDiscordGuild {
    private String customerId;
    private String guildId;
    private List<String> usersRoles;
    private List<String> moderatorsRoles;
}
