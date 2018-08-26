package com.trevorgowing.tasklist.task;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractControllerTests;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TaskControllerDeleteTests extends AbstractControllerTests {

  @Mock private TaskDeleter taskDeleter;

  @InjectMocks private TaskController taskController;

  @Override
  protected Object getController() {
    return taskController;
  }

  @Test(expected = UserNotFoundException.class)
  public void testDeleteWithNoMatchingUser_shouldRespondWithStatusConflictAndExceptionResponse() {
    Long userId = 1L;
    Long taskId = 2L;
    String message = "User not found for id: \'1\'";

    ExceptionResponse conflictResponse =
        ExceptionResponse.builder().status(CONFLICT).message(message).build();

    doThrow(UserNotFoundException.builder().message(message).build())
        .when(taskDeleter)
        .deleteByUserIdAndTaskId(userId, taskId);

    given()
        .delete("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(CONFLICT.value())
        .body(is(equalTo(JsonEncoder.encodeToString(conflictResponse))));

    taskController.delete(userId, taskId);
  }

  @Test(expected = TaskNotFoundException.class)
  public void testDeleteWithNoMatchingTask_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    Long userId = 1L;
    Long taskId = 2L;
    String message = "Task not found for id: \'2\'";

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message(message).build();

    doThrow(TaskNotFoundException.builder().message(message).build())
        .when(taskDeleter)
        .deleteByUserIdAndTaskId(userId, taskId);

    given()
        .delete("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));

    taskController.delete(userId, taskId);
  }

  @Test
  public void testDeleteWithMatchingUser_shouldRespondWithStatusCreatedAndTask() {
    Long userId = 1L;
    Long taskId = 2L;

    doNothing().when(taskDeleter).deleteByUserIdAndTaskId(userId, taskId);

    given()
        .delete("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(NO_CONTENT.value());

    taskController.delete(userId, taskId);
  }
}
