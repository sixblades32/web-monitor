package io.enigmasolutions.webmonitor.webbroadcastservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message<T> {

    private Timeline timeline;
    private T data;
}
