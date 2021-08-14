package io.enigmasolutions.twittermonitor.exceptions;

public class NoTwitterUserMatchesException extends RuntimeException {
    public NoTwitterUserMatchesException() {
        super("Twitter User not found!");
    }
}
