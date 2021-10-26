package io.enigmasolutions.twittermonitor.exceptions;

public class NoAvailableProxyException extends RuntimeException{
    public NoAvailableProxyException() {
        super("There is no available proxy!");
    }
}
