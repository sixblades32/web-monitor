package io.enigmasolutions.twittermonitor.models.external;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProxyUpdatingErrorResponse extends  ErrorResponse{
    private Integer requiredNumber;

    public ProxyUpdatingErrorResponse(String result, int requiredCount) {
        super(result);
        this.requiredNumber = requiredCount;
    }
}
