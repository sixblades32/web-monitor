package io.enigmasolutions.broadcastmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscordUser {
    private String name;
    private String tag;
    private String icon;
}
