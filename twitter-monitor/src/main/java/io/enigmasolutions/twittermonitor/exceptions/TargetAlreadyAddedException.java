package io.enigmasolutions.twittermonitor.exceptions;

public class TargetAlreadyAddedException extends RuntimeException{
    public TargetAlreadyAddedException() {
        super("Target already added!");
    }
}
