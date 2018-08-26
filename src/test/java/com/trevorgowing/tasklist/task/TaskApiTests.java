package com.trevorgowing.tasklist.task;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractSpringWebContextTests;
import com.trevorgowing.tasklist.user.User;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({TestTaskFinder.class})
public class TaskApiTests extends AbstractSpringWebContextTests {

  @Autowired private EntityManager entityManager;
  @Autowired private TestTaskFinder testTaskFinder;

  @Test
  public void testGetWithNoExistingTasks_shouldRespondWithStatusOkAndEmptyArray() {
    User user = User.builder().username("user").build();
    entityManager.persist(user);

    given()
        .accept(JSON)
        .get(String.format("/api/user/%s/task", user.getId()))
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(Collections.emptyList()))));
  }

  @Test
  public void testGetWithExistingTasks_shouldRespondWithStatusOkAndTaskArray() {
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    Task taskOne = Task.builder().user(user).name("one").dateTime(date).build();
    entityManager.persist(taskOne);
    TaskDTO taskOneDTO = TaskDTO.builder().id(taskOne.getId()).name("one").dateTime(date).build();

    Task taskTwo = Task.builder().user(user).name("two").dateTime(date).build();
    entityManager.persist(taskTwo);
    TaskDTO taskTwoDTO = TaskDTO.builder().id(taskTwo.getId()).name("two").dateTime(date).build();

    Collection<TaskDTO> expected = Arrays.asList(taskOneDTO, taskTwoDTO);

    given()
        .accept(JSON)
        .get(String.format("/api/user/%s/task", user.getId()))
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(expected))));
  }

  @Test
  public void testGetByIdWithNoMatchingTask_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    Long taskId = 1000L;
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    Task taskOne = Task.builder().user(user).name("one").dateTime(date).build();
    entityManager.persist(taskOne);

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder()
            .status(NOT_FOUND)
            .message(
                String.format(
                    "Task not found for userId: \'%s\' and taskId: \'%s\'", user.getId(), taskId))
            .build();

    given()
        .accept(JSON)
        .get(String.format("/api/user/%s/task/%s", user.getId(), taskId))
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));
  }

  @Test
  public void testGetByIdWithMatchingTask_shouldRespondWithStatusOkAndTask() {
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    Task taskOne = Task.builder().user(user).name("one").dateTime(date).build();
    entityManager.persist(taskOne);
    TaskDTO taskOneDTO = TaskDTO.builder().id(taskOne.getId()).name("one").dateTime(date).build();

    given()
        .accept(JSON)
        .get(String.format("/api/user/%s/task/%s", user.getId(), taskOne.getId()))
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(taskOneDTO))));
  }

  @Test
  public void testPostWithNoMatchingUser_shouldRespondWithStatusConflictAndExceptionResponse() {
    ExceptionResponse conflictResponse =
        ExceptionResponse.builder()
            .status(CONFLICT)
            .message("User not found for id: \'1\'")
            .build();

    TaskDTO requestTask = TaskDTO.builder().name("task").dateTime(LocalDateTime.now()).build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(requestTask))
        .post("/api/user/1/task")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(CONFLICT.value())
        .body(is(equalTo(JsonEncoder.encodeToString(conflictResponse))));
  }

  @Test
  public void testPostWithValidTask_shouldRespondWithStatusCreatedAndTask() {
    String name = "name";
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    TaskDTO requestTask = TaskDTO.builder().name(name).dateTime(date).build();

    MockMvcResponse response =
        given()
            .accept(JSON)
            .contentType(JSON)
            .body(JsonEncoder.encodeToString(requestTask))
            .post(String.format("/api/user/%s/task", user.getId()));

    Task task = testTaskFinder.findByName(name);

    TaskDTO responseTaskDTO =
        TaskDTO.builder().id(task.getId()).name(task.getName()).dateTime(date).build();

    response
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(CREATED.value())
        .body(is(equalTo(JsonEncoder.encodeToString(responseTaskDTO))));
  }

  @Test
  public void testPutWithNoMatchingUser_shouldRespondWithStatusConflictAndExceptionResponse() {
    ExceptionResponse conflictResponse =
        ExceptionResponse.builder()
            .status(CONFLICT)
            .message("User not found for id: \'1\'")
            .build();

    TaskDTO requestTask = TaskDTO.builder().name("task").dateTime(LocalDateTime.now()).build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(requestTask))
        .put("/api/user/1/task/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(CONFLICT.value())
        .body(is(equalTo(JsonEncoder.encodeToString(conflictResponse))));
  }

  @Test
  public void testPutWithNoMatchingTask_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    Long taskId = 1000L;
    String name = "test";
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    TaskDTO requestTaskDTO = TaskDTO.builder().name(name).dateTime(date).build();

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder()
            .status(NOT_FOUND)
            .message(String.format("Task not found for id: \'%s\'", taskId))
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(requestTaskDTO))
        .put(String.format("/api/user/%s/task/%s", user.getId(), taskId))
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));
  }

  @Test
  public void testPutWithValidTask_shouldRespondWithStatusOkAndTask() {
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    Task existingTask = Task.builder().user(user).name("other").dateTime(date).build();
    entityManager.persist(existingTask);

    String name = "test";
    TaskDTO requestTaskDTO = TaskDTO.builder().name(name).dateTime(date).build();

    MockMvcResponse response =
        given()
            .accept(JSON)
            .contentType(JSON)
            .body(JsonEncoder.encodeToString(requestTaskDTO))
            .put(String.format("/api/user/%s/task/%s", user.getId(), existingTask.getId()));

    Task task = testTaskFinder.findByName(name);

    TaskDTO responseTaskDTO =
        TaskDTO.builder().id(task.getId()).name(task.getName()).dateTime(date).build();

    response
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(responseTaskDTO))));
  }

  @Test
  public void testDeleteWithNoMatchingUser_shouldRespondWithStatusConflictAndExceptionResponse() {
    ExceptionResponse conflictResponse =
        ExceptionResponse.builder()
            .status(CONFLICT)
            .message("User not found for id: \'1\'")
            .build();

    given()
        .delete("/api/user/1/task/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(CONFLICT.value())
        .body(is(equalTo(JsonEncoder.encodeToString(conflictResponse))));
  }

  @Test
  public void
      testDeleteByIdWithNoMatchingTask_shouldResponseWithStatusNotFoundAndExceptionResponse() {
    Long taskId = 1000L;
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    Task taskOne = Task.builder().user(user).name("one").dateTime(date).build();
    entityManager.persist(taskOne);

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder()
            .status(NOT_FOUND)
            .message(String.format("Task not found for id: \'%s\'", taskId))
            .build();

    given()
        .accept(JSON)
        .delete(String.format("/api/user/%s/task/%s", user.getId(), taskId))
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));
  }

  @Test
  public void testDeleteByIdWithMatchingTask_shouldRespondWithStatusNoContent() {
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    Task taskOne = Task.builder().user(user).name("one").dateTime(date).build();
    entityManager.persist(taskOne);

    given()
        .accept(JSON)
        .delete(String.format("/api/user/%s/task/%s", user.getId(), taskOne.getId()))
        .then()
        .log()
        .ifValidationFails()
        .statusCode(NO_CONTENT.value());
  }
}
