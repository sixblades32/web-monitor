package io.enigmasolutions.twittermonitor.models.external;

import lombok.Data;

@Data
public class RestResponse {

    private String result;

    public RestResponse(String result) {
        this.result = result;
    }
}
