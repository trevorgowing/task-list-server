package com.trevorgowing.tasklist.user;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.common.validation.Create;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
class UserController {

  private final UserFinder userFinder;
  private final UserCreator userCreator;

  @ResponseStatus(OK)
  @SuppressWarnings("unused")
  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  Collection<UserDTO> get() {
    return userFinder.findDTOs();
  }

  @ResponseStatus(OK)
  @SuppressWarnings("unused")
  @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
  UserDTO get(@PathVariable Long id) {
    return userFinder.findDTOById(id);
  }

  @ResponseStatus(CREATED)
  @SuppressWarnings("unused")
  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  UserDTO post(@RequestBody @Validated(Create.class) UserDTO userDTO) {
    return UserDTO.from(userCreator.create(userDTO));
  }

  @SuppressWarnings("unused")
  @ExceptionHandler(UserNotFoundException.class)
  ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException unfe) {
    return ResponseEntity.status(NOT_FOUND)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(NOT_FOUND, unfe.getMessage()));
  }

  @SuppressWarnings("unused")
  @ExceptionHandler(DuplicateUsernameException.class)
  ResponseEntity<ExceptionResponse> handleDuplicateUsernameException(
      DuplicateUsernameException due) {
    return ResponseEntity.status(CONFLICT)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(CONFLICT, due.getMessage()));
  }
}
