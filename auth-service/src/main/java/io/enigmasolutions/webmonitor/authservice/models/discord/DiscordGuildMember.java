package io.enigmasolutions.webmonitor.authservice.models.discord;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscordGuildMember {

  private List<String> roles;
}
