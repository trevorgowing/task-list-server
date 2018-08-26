package com.trevorgowing.tasklist.task;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.type.AbstractControllerTests;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;

public class TaskControllerExceptionHandlerTests extends AbstractControllerTests {

  @InjectMocks private TaskController taskController;

  @Override
  protected Object getController() {
    return taskController;
  }

  @Test
  public void
      testHandleTaskNotFoundException_shouldReturnResponseEntityContainingExceptionResponse() {
    String message = "Task not found for id: \'1\'";

    ResponseEntity expected =
        ResponseEntity.status(NOT_FOUND)
            .contentType(APPLICATION_JSON_UTF8)
            .body(ExceptionResponse.builder().status(NOT_FOUND).message(message).build());

    TaskNotFoundException taskNotFoundException =
        TaskNotFoundException.builder().message(message).build();

    ResponseEntity actual = taskController.handleTaskNotFoundException(taskNotFoundException);

    MatcherAssert.assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void
      testHandleUserNotFoundException_shouldReturnResponseEntityContainingExceptionResponse() {
    String message = "User not found for id: \'1\'";

    ResponseEntity expected =
        ResponseEntity.status(CONFLICT)
            .contentType(APPLICATION_JSON_UTF8)
            .body(ExceptionResponse.builder().status(CONFLICT).message(message).build());

    UserNotFoundException userNotFoundException =
        UserNotFoundException.builder().message(message).build();

    ResponseEntity actual = taskController.handleUserNotFoundException(userNotFoundException);

    MatcherAssert.assertThat(actual, is(equalTo(expected)));
  }
}
