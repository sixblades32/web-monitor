package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.exceptions.MonitorRunningException;
import io.enigmasolutions.twittermonitor.exceptions.NoTargetMatchesException;
import io.enigmasolutions.twittermonitor.exceptions.NoTwitterUserMatchesException;
import io.enigmasolutions.twittermonitor.exceptions.TargetAlreadyAddedException;
import io.enigmasolutions.twittermonitor.models.external.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(MonitorRunningException.class)
    public ResponseEntity<RestResponse> handleMonitorRunningException (MonitorRunningException e){
        RestResponse response = new RestResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoTwitterUserMatchesException.class)
    public ResponseEntity<RestResponse> handleUserMatchesException(NoTwitterUserMatchesException e){
        RestResponse response = new RestResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoTargetMatchesException.class)
    public ResponseEntity<RestResponse> handleTargetMatchesException(NoTargetMatchesException e){
        RestResponse response = new RestResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TargetAlreadyAddedException.class)
    public ResponseEntity<RestResponse> handleTargetAlreadyAddedException(TargetAlreadyAddedException e){
        RestResponse response = new RestResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}
