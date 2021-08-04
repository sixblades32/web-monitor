package io.enigmasolutions.dictionarymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Jacksonized
public class CustomerDiscordBroadcast {
    private List<String> baseWebhooks;
    private List<String> liveWebhooks;
}

