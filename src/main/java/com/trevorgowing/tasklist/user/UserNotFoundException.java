package com.trevorgowing.tasklist.user;

import lombok.Builder;

public class UserNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 7345513674315146043L;

  @Builder
  private UserNotFoundException(String message) {
    super(message);
  }

  public static UserNotFoundException causedBy(String message) {
    return new UserNotFoundException(message);
  }
}
