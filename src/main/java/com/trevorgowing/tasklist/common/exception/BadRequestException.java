package com.trevorgowing.tasklist.common.exception;

import lombok.Builder;

public class BadRequestException extends RuntimeException {

  private static final long serialVersionUID = 6227728820669269141L;

  @Builder
  private BadRequestException(String message) {
    super(message);
  }

  public static BadRequestException causedBy(String message) {
    return new BadRequestException(message);
  }
}
