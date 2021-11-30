package io.enigmasolutions.twittermonitor.exceptions;

public class TargetIsPrivateException extends RuntimeException{

    public TargetIsPrivateException() {
        super("Target is private, please wait until this account leaves private!");
    }
}
