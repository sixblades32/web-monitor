package io.enigmasolutions.twittermonitor.exceptions;

public class MonitorRunningException extends RuntimeException {
    public MonitorRunningException() {
        super("Monitor is running");
    }
}
