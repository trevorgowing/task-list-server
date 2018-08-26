package com.trevorgowing.tasklist.task;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractControllerTests;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import java.time.LocalDateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TaskControllerPutTests extends AbstractControllerTests {

  private static final String TOO_LONG =
      "oYeFSSnuajlKSHf1xhRYPLqSQmSteW9LchrdSOxDoPkpm1XtH2gZQIs2c"
          + "vQekub5X0BKZx68n4h3CnxjXKPeUoXMW7qDS0YamziSE0t9t5gTN5LCxBmqOWIVd3jya8TlRueXpy0QHo0JhpWkWKF"
          + "ubK50MAkSSQIl5jEEqupWd4fgUPk3sr7aMGbkqpOiVstGfOygzal2iSJxN1OBfaCknJFfDpyHQR1QtjjNa4XFJ2UXy"
          + "BEBdN7wZBCGIaBMSCigb";

  @Mock private TaskModifier taskModifier;

  @InjectMocks private TaskController taskController;

  @Override
  protected Object getController() {
    return taskController;
  }

  @Test
  public void testPutWithNameTooShort_shouldRespondWithStatusBadRequestAndExceptionResponse() {
    TaskDTO taskDTO = TaskDTO.builder().name("").dateTime(LocalDateTime.now()).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("\'name\' length expected between 1 and 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(taskDTO))
        .put("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPutWithNameToLong_shouldRespondWithStatusBadRequestAndExceptionResponse() {
    TaskDTO taskDTO = TaskDTO.builder().name(TOO_LONG).dateTime(LocalDateTime.now()).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("\'name\' length expected between 1 and 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(taskDTO))
        .put("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void
      testPutWithDescriptionTooShort_shouldRespondWithStatusBadRequestAndExceptionResponse() {
    TaskDTO taskDTO =
        TaskDTO.builder().name("task").description("").dateTime(LocalDateTime.now()).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("\'description\' length expected between 1 and 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(taskDTO))
        .put("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPutWithDescriptionToLong_shouldRespondWithStatusBadRequestAndExceptionResponse() {
    TaskDTO taskDTO =
        TaskDTO.builder().name("task").description(TOO_LONG).dateTime(LocalDateTime.now()).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("\'description\' length expected between 1 and 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(taskDTO))
        .put("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test(expected = UserNotFoundException.class)
  public void testPutWithNoMatchingUser_shouldRespondWithStatusConflictAndExceptionResponse() {
    Long userId = 1L;
    Long taskId = 2L;
    String message = "User not found for id: \'1\'";

    TaskDTO taskDTO = TaskDTO.builder().name("task").dateTime(LocalDateTime.now()).build();

    ExceptionResponse conflictResponse =
        ExceptionResponse.builder().status(CONFLICT).message(message).build();

    when(taskModifier.modify(userId, taskId, taskDTO))
        .thenThrow(UserNotFoundException.builder().message(message).build());

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(taskDTO))
        .put("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(CONFLICT.value())
        .body(is(equalTo(JsonEncoder.encodeToString(conflictResponse))));

    taskController.put(userId, taskId, taskDTO);
  }

  @Test(expected = TaskNotFoundException.class)
  public void testPutWithNoMatchingTask_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    Long userId = 1L;
    Long taskId = 2L;
    String message = "Task not found for id: \'2\'";

    TaskDTO taskDTO = TaskDTO.builder().name("task").dateTime(LocalDateTime.now()).build();

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message(message).build();

    when(taskModifier.modify(userId, taskId, taskDTO))
        .thenThrow(TaskNotFoundException.builder().message(message).build());

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(taskDTO))
        .put("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));

    taskController.put(userId, taskId, taskDTO);
  }

  @Test
  public void testPutWithMatchingUser_shouldRespondWithStatusCreatedAndTask() {
    Long userId = 1L;
    Long taskId = 2L;
    LocalDateTime date = LocalDateTime.now();

    TaskDTO taskDTO = TaskDTO.builder().name("task").dateTime(date).build();

    Task task = Task.builder().id(2L).name("task").dateTime(date).build();

    TaskDTO expected = TaskDTO.builder().id(2L).name("task").dateTime(date).build();

    when(taskModifier.modify(userId, taskId, taskDTO)).thenReturn(task);

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(taskDTO))
        .put("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToTimestampString(expected))));

    TaskDTO actual = taskController.put(userId, taskId, taskDTO);

    assertThat(actual, is(equalTo(expected)));
  }
}
