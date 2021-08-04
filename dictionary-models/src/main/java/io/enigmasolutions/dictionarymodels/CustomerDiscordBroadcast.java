package io.enigmasolutions.dictionarymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CustomerDiscordBroadcast {
    private List<String> baseWebhooks;
    private List<String> liveWebhooks;
}

