package io.enigmasolutions.twittermonitor.controllers;

import io.enigmasolutions.twittermonitor.exceptions.*;
import io.enigmasolutions.twittermonitor.models.external.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler(MonitorRunningException.class)
  public ResponseEntity<ErrorResponse> handleMonitorRunningException(MonitorRunningException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(NoTwitterUserMatchesException.class)
  public ResponseEntity<ErrorResponse> handleUserMatchesException(NoTwitterUserMatchesException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(NoTargetMatchesException.class)
  public ResponseEntity<ErrorResponse> handleTargetMatchesException(NoTargetMatchesException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TargetAlreadyAddedException.class)
  public ResponseEntity<ErrorResponse> handleTargetAlreadyAddedException(
      TargetAlreadyAddedException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(NoAvailableProxyException.class)
  public ResponseEntity<ErrorResponse> handleNoAvailableProxyException(
          NoAvailableProxyException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TargetIsPrivateException.class)
  public ResponseEntity<ErrorResponse> handleTargetIsPrivateException(
          TargetIsPrivateException e) {
    ErrorResponse response = new ErrorResponse(e.getMessage());

    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
  }
}
