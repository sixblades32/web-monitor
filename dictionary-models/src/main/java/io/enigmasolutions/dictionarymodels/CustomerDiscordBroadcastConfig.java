package io.enigmasolutions.dictionarymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDiscordBroadcastConfig {

    private CustomerDiscordBroadcast customerDiscordBroadcast;
    private CustomerTheme theme;
}
