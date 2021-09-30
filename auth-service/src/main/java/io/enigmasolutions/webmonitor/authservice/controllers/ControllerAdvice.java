package io.enigmasolutions.webmonitor.authservice.controllers;

import io.enigmasolutions.webmonitor.authservice.exceptions.DeprecatedTokenException;
import io.enigmasolutions.webmonitor.authservice.exceptions.NoMutualGuildException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler(value = {DeprecatedTokenException.class, NoMutualGuildException.class})
  @ResponseStatus(value = HttpStatus.FORBIDDEN)
  public void resourceNotFoundException(Exception ex) {
    log.error(ex.getMessage());
  }
}
