package io.enigmasolutions.webmonitor.dictionaryservice.db.models.references;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscordGuild {

  private String guildId;
  private String channelId;
  private List<String> usersRoles;
  private List<String> moderatorsRoles;
}
