package io.enigmasolutions.broadcastmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
