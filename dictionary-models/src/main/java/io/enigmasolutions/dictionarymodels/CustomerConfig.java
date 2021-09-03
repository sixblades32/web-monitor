package io.enigmasolutions.dictionarymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerConfig {
    private CustomerDiscordBroadcast customerDiscordBroadcast;
    private CustomerDiscordGuild customerDiscordGuild;
    private CustomerTheme theme;
}
