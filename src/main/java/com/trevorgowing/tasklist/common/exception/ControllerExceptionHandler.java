package com.trevorgowing.tasklist.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ExceptionResponse> handleBadRequestException(BadRequestException bre) {
    log.debug(bre.getMessage(), bre);

    return ResponseEntity.status(BAD_REQUEST)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(BAD_REQUEST, bre.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleUnhandledException(Exception exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(INTERNAL_SERVER_ERROR, exception.getMessage()));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException manve,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    log.debug(manve.getMessage(), manve);

    String message =
        manve
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));

    return ResponseEntity.status(BAD_REQUEST)
        .contentType(APPLICATION_JSON_UTF8)
        .headers(headers)
        .body(ExceptionResponse.from(BAD_REQUEST, message));
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception exception,
      Object body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    if (status.is4xxClientError()) {
      log.debug(exception.getMessage(), exception);
    } else if (status.is5xxServerError()) {
      log.error(exception.getMessage(), exception);
    }

    return ResponseEntity.status(status)
        .contentType(APPLICATION_JSON_UTF8)
        .headers(headers)
        .body(ExceptionResponse.from(status, exception.getMessage()));
  }
}
