package com.trevorgowing.tasklist.common.exception;

import lombok.Builder;

public class AccessDeniedException extends RuntimeException {

  private static final long serialVersionUID = 183158968388858118L;

  @Builder
  private AccessDeniedException(String message) {
    super(message);
  }

  public static AccessDeniedException causedBy(String message) {
    return new AccessDeniedException(message);
  }
}
