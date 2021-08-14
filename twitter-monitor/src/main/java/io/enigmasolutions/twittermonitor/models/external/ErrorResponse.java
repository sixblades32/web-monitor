package io.enigmasolutions.twittermonitor.models.external;

import lombok.Data;

@Data
public class ErrorResponse {

    private String result;

    public ErrorResponse(String result) {
        this.result = result;
    }
}
