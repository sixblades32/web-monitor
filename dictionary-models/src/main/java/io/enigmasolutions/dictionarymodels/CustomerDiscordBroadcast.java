package io.enigmasolutions.dictionarymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDiscordBroadcast {
    private List<String> baseWebhooks;
    private List<String> staffBaseWebhooks;
    private List<String> liveWebhooks;
}

