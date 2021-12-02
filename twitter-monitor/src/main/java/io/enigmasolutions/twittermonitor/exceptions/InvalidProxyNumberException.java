package io.enigmasolutions.twittermonitor.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidProxyNumberException extends RuntimeException {
    private int requiredNumber;

    public InvalidProxyNumberException(int requiredNumber) {
        super("The number of proxies does not match the number of follow scrappers!");
        this.requiredNumber = requiredNumber;
    }
}
