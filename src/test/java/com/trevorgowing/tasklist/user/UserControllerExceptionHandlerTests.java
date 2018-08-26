package com.trevorgowing.tasklist.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;

public class UserControllerExceptionHandlerTests extends AbstractTests {

  @InjectMocks private UserController userController;

  @Test
  public void
      testHandleUserNotFoundException_shouldReturnResponseEntityContainingExceptionResponse() {
    String message = "User not found for id: \'1\'";

    ResponseEntity expected =
        ResponseEntity.status(NOT_FOUND)
            .contentType(APPLICATION_JSON_UTF8)
            .body(ExceptionResponse.builder().status(NOT_FOUND).message(message).build());

    UserNotFoundException userNotFoundException =
        UserNotFoundException.builder().message(message).build();

    ResponseEntity actual = userController.handleUserNotFoundException(userNotFoundException);

    MatcherAssert.assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void
      testHandleDuplicateUsernameException_shouldReturnResponseEntityContainingExceptionResponse() {
    String message = "User with username: \'test\' already exists";

    ResponseEntity expected =
        ResponseEntity.status(CONFLICT)
            .contentType(APPLICATION_JSON_UTF8)
            .body(ExceptionResponse.builder().status(CONFLICT).message(message).build());

    DuplicateUsernameException duplicateUsernameException =
        DuplicateUsernameException.builder().message(message).build();

    ResponseEntity actual =
        userController.handleDuplicateUsernameException(duplicateUsernameException);

    assertThat(actual, is(equalTo(expected)));
  }
}
