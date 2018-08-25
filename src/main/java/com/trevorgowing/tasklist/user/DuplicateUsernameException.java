package com.trevorgowing.tasklist.user;

import lombok.Builder;

class DuplicateUsernameException extends RuntimeException {

  private static final long serialVersionUID = 2635701916050006968L;

  @Builder
  private DuplicateUsernameException(String message) {
    super(message);
  }

  static DuplicateUsernameException causedBy(String message) {
    return new DuplicateUsernameException(message);
  }
}
