package com.trevorgowing.tasklist.user;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.trevorgowing.tasklist.common.exception.BadRequestException;
import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractControllerTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserControllerPutTests extends AbstractControllerTests {

  private static final String TOO_LONG =
      "oYeFSSnuajlKSHf1xhRYPLqSQmSteW9LchrdSOxDoPkpm1XtH2gZQIs2c"
          + "vQekub5X0BKZx68n4h3CnxjXKPeUoXMW7qDS0YamziSE0t9t5gTN5LCxBmqOWIVd3jya8TlRueXpy0QHo0JhpWkWKF"
          + "ubK50MAkSSQIl5jEEqupWd4fgUPk3sr7aMGbkqpOiVstGfOygzal2iSJxN1OBfaCknJFfDpyHQR1QtjjNa4XFJ2UXy"
          + "BEBdN7wZBCGIaBMSCigb";

  @Mock private UserModifier userModifier;

  @InjectMocks private UserController userController;

  @Override
  protected Object getController() {
    return userController;
  }

  @Test
  public void testPutWithNullId_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().id(null).username("test").build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("\'id\' may not be null, use POST to create")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPutWithNullUsername_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().id(1L).username(null).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder().status(BAD_REQUEST).message("\'username\' is required").build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPutWithUsernameTooShort_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().id(1L).username("u").build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("'username' length expected between 3 and 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPutWithUsernameTooLong_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().id(1L).username("test").username(TOO_LONG).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("'username' length expected between 3 and 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPutWithFirstNameTooLong_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().id(1L).username("test").firstName(TOO_LONG).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("'first_name' length may not exceed 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPutWithLastNameTooLong_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().id(1L).username("test").lastName(TOO_LONG).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("'last_name' length may not exceed 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test(expected = BadRequestException.class)
  public void testPutWithMismatchedIds_shouldResponseWithStatusBadRequestAndExceptionResponse() {
    UserDTO userDTO = UserDTO.builder().id(2L).username("test").build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("Path variable User#id is not equal to request body User#id")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));

    userController.put(1L, userDTO);
  }

  @Test(expected = UserNotFoundException.class)
  public void testPutWithNoMatchingUser_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    UserDTO requestUserDTO =
        UserDTO.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    String message = "User not found for id: \'%s\'";

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message(message).build();

    when(userModifier.replace(requestUserDTO))
        .thenThrow(UserNotFoundException.builder().message(message).build());

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(requestUserDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));

    userController.put(1L, requestUserDTO);
  }

  @Test
  public void testPutWithValidUser_shouldRespondWithStatusOkAndReplacedUser() {
    UserDTO requestUserDTO =
        UserDTO.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    User user = User.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    UserDTO expected =
        UserDTO.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    when(userModifier.replace(requestUserDTO)).thenReturn(user);

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(requestUserDTO))
        .put("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(expected))));

    UserDTO actual = userController.put(1L, requestUserDTO);

    assertThat(actual, is(equalTo(expected)));
  }
}
