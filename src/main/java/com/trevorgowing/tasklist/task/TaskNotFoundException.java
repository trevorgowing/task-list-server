package com.trevorgowing.tasklist.task;

import lombok.Builder;

class TaskNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -5077581966599342000L;

  @Builder
  private TaskNotFoundException(String message) {
    super(message);
  }

  static TaskNotFoundException causedBy(String message) {
    return new TaskNotFoundException(message);
  }
}
