package io.enigmasolutions.webmonitor.webbroadcastservice.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {

    private String name;
    private int count;
}
