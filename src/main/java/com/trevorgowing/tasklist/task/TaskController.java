package com.trevorgowing.tasklist.task;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.common.validation.Create;
import com.trevorgowing.tasklist.common.validation.Modify;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/{userId}/task")
class TaskController {

  private final TaskFinder taskFinder;
  private final TaskCreator taskCreator;
  private final TaskModifier taskModifier;
  private final TaskDeleter taskDeleter;

  @ResponseStatus(OK)
  @SuppressWarnings("unused")
  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  Collection<TaskDTO> get(@PathVariable Long userId) {
    return taskFinder.findDTOsByUserId(userId);
  }

  @ResponseStatus(OK)
  @SuppressWarnings("unused")
  @GetMapping(path = "/{taskId}", produces = APPLICATION_JSON_UTF8_VALUE)
  TaskDTO get(@PathVariable Long userId, @PathVariable Long taskId) {
    return taskFinder.findDTOByUserIdAndTaskId(userId, taskId);
  }

  @ResponseStatus(CREATED)
  @SuppressWarnings("unused")
  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  TaskDTO post(@PathVariable Long userId, @RequestBody @Validated(Create.class) TaskDTO taskDTO) {
    return TaskDTO.from(taskCreator.create(userId, taskDTO));
  }

  @ResponseStatus(OK)
  @SuppressWarnings("unused")
  @PutMapping(
    path = "/{taskId}",
    consumes = APPLICATION_JSON_UTF8_VALUE,
    produces = APPLICATION_JSON_UTF8_VALUE
  )
  TaskDTO put(
      @PathVariable Long userId,
      @PathVariable Long taskId,
      @Validated(Modify.class) @RequestBody TaskDTO taskDTO) {
    return TaskDTO.from(taskModifier.modify(userId, taskId, taskDTO));
  }

  @ResponseStatus(NO_CONTENT)
  @SuppressWarnings("unused")
  @DeleteMapping(path = "/{taskId}")
  void delete(@PathVariable Long userId, @PathVariable Long taskId) {
    taskDeleter.deleteByUserIdAndTaskId(userId, taskId);
  }

  @SuppressWarnings("unused")
  @ExceptionHandler(TaskNotFoundException.class)
  ResponseEntity<ExceptionResponse> handleTaskNotFoundException(TaskNotFoundException tnfe) {
    log.debug(tnfe.getMessage(), tnfe);
    return ResponseEntity.status(NOT_FOUND)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(NOT_FOUND, tnfe.getMessage()));
  }

  @SuppressWarnings("unused")
  @ExceptionHandler(UserNotFoundException.class)
  ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException unfe) {
    log.debug(unfe.getMessage(), unfe);
    return ResponseEntity.status(CONFLICT)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(CONFLICT, unfe.getMessage()));
  }
}
