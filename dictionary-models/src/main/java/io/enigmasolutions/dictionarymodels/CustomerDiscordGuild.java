package io.enigmasolutions.dictionarymodels;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDiscordGuild {

  private String customerId;
  private String channelId;
  private String guildId;
  private List<String> usersRoles;
  private List<String> moderatorsRoles;
}
