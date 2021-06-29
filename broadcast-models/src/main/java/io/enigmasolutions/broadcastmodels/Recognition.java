package io.enigmasolutions.broadcastmodels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recognition {
    private String source;
    private String result;
}
