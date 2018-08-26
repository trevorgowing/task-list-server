package com.trevorgowing.tasklist.task;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractControllerTests;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TaskControllerGetTests extends AbstractControllerTests {

  @Mock private TaskFinder taskFinder;

  @InjectMocks private TaskController taskController;

  @Override
  protected Object getController() {
    return taskController;
  }

  @Test
  public void testGetWithNoExistingTasks_shouldRespondWithStatusOkAndEmptyArray() {
    Long userId = 1L;

    when(taskFinder.findDTOsByUserId(userId)).thenReturn(Collections.emptyList());

    given()
        .accept(JSON)
        .get("/api/user/1/task")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(Collections.emptyList()))));

    taskController.get(userId);
  }

  @Test
  public void testGetWithExistingTasks_shouldRespondWithOkAndTaskDTOs() {
    Long userId = 1L;

    TaskDTO taskOneDTO = TaskDTO.builder().id(1L).name("one").build();
    TaskDTO taskTwoDTO = TaskDTO.builder().id(2L).name("two").build();

    Collection<TaskDTO> expected = Arrays.asList(taskOneDTO, taskTwoDTO);

    when(taskFinder.findDTOsByUserId(userId)).thenReturn(expected);

    given()
        .accept(JSON)
        .get("/api/user/1/task")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(expected))));

    Collection<TaskDTO> actual = taskController.get(userId);

    assertThat(actual, hasItems(taskOneDTO, taskTwoDTO));
  }

  @Test(expected = TaskNotFoundException.class)
  public void
      testGetByUserIdAndTaskIdWithNoMatchingTask_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    Long userId = 1L;
    Long taskId = 2L;

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message("Task not found").build();

    when(taskFinder.findDTOByUserIdAndTaskId(userId, taskId))
        .thenThrow(TaskNotFoundException.builder().message("Task not found").build());

    given()
        .accept(JSON)
        .get("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));

    taskController.get(userId, taskId);
  }

  @Test
  public void testGetByUserIdAndTaskIdWithMatchingUser_shouldRespondWithStatusOkAndTask() {
    Long userId = 1L;
    Long taskId = 2L;

    TaskDTO expected = TaskDTO.builder().id(taskId).name("task").build();

    when(taskFinder.findDTOByUserIdAndTaskId(userId, taskId)).thenReturn(expected);

    given()
        .accept(JSON)
        .get("/api/user/1/task/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(expected))));

    TaskDTO actual = taskController.get(userId, taskId);

    assertThat(actual, is(equalTo(expected)));
  }
}
