package com.trevorgowing.tasklist.common.exception;

import java.io.Serializable;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public final class ExceptionResponse implements Serializable {

  private static final long serialVersionUID = 3014824356664533708L;

  private final int status;
  private final String error;
  private final String message;

  @Builder
  private ExceptionResponse(HttpStatus status, String message) {
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.message = message;
  }

  public static ExceptionResponse from(HttpStatus status, String message) {
    return new ExceptionResponse(status, message);
  }
}
