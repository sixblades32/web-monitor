package io.enigmasolutions.twittermonitor.models.twitter.graphQL;

import lombok.Data;

import java.util.List;

@Data
public class NestedTimeline {
    private List<Instruction> instructions;
}
