package io.enigmasolutions.broadcastmodels;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Staff {

  private StaffType type;
  private String text;
  private DiscordUser discordUser;
  private List<String> detectedUrls;
}
