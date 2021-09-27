package io.enigmasolutions.dictionarymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTheme {
    private Boolean isCustom;
    private Integer tweetColor;
    private Integer retweetColor;
    private Integer replyColor;
    private String generalColor;
    private String logoUrl;
}
