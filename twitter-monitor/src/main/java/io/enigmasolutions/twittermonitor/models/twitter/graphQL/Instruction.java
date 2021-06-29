package io.enigmasolutions.twittermonitor.models.twitter.graphql;

import lombok.Data;

import java.util.List;

@Data
public class Instruction {
    private List<Entry> entries;
}
