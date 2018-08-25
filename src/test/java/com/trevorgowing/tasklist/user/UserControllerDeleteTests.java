package com.trevorgowing.tasklist.user;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractControllerTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserControllerDeleteTests extends AbstractControllerTests {

  @Mock private UserDeleter userDeleter;

  @InjectMocks private UserController userController;

  @Override
  protected Object getController() {
    return userController;
  }

  @Test(expected = UserNotFoundException.class)
  public void
      testDeleteByIdWithNoMatchingUser_shouldThrowResponsdWithStatusNotFoundAndExceptionResponse() {
    String message = "User not found for id: 1";

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message(message).build();

    doThrow(UserNotFoundException.builder().message(message).build())
        .when(userDeleter)
        .deleteById(1L);

    given()
        .accept(JSON)
        .delete("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));

    userController.delete(1L);
  }

  @Test
  public void
      testGetByIdWithMatchingUser_shouldDelegateToUserFinderAndResponseWithStatusOkAndUser() {
    Long id = 1L;

    doNothing().when(userDeleter).deleteById(1L);

    given()
        .accept(JSON)
        .delete("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(NO_CONTENT.value());

    userController.delete(id);
  }
}
