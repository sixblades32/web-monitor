package io.enigmasolutions.twittermonitor.exceptions;

public class NoTargetMatchesException extends RuntimeException {
    public NoTargetMatchesException() {
        super("Target not found!");
    }
}
