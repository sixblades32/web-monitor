package io.enigmasolutions.webmonitor.webbroadcastservice.models.external;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message<T> {

    private Timeline timeline;
    private String timestamp;
    private T data;
}
