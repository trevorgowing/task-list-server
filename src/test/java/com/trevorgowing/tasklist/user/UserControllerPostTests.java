package com.trevorgowing.tasklist.user;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractControllerTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserControllerPostTests extends AbstractControllerTests {

  private static final String TOO_LONG =
      "oYeFSSnuajlKSHf1xhRYPLqSQmSteW9LchrdSOxDoPkpm1XtH2gZQIs2c"
          + "vQekub5X0BKZx68n4h3CnxjXKPeUoXMW7qDS0YamziSE0t9t5gTN5LCxBmqOWIVd3jya8TlRueXpy0QHo0JhpWkWKF"
          + "ubK50MAkSSQIl5jEEqupWd4fgUPk3sr7aMGbkqpOiVstGfOygzal2iSJxN1OBfaCknJFfDpyHQR1QtjjNa4XFJ2UXy"
          + "BEBdN7wZBCGIaBMSCigb";

  @Mock private UserCreator userCreator;

  @InjectMocks private UserController userController;

  @Override
  protected Object getController() {
    return userController;
  }

  @Test
  public void testPostWithNonNullId_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().id(1L).username("test").build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("\'id\' must be null, use PUT to replace")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .post("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPostWithNullUsername_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().username(null).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder().status(BAD_REQUEST).message("\'username\' is required").build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .post("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPostWithUsernameTooShort_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().username("u").build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("'username' length expected between 3 and 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .post("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPostWithUsernameTooLong_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().username("test").username(TOO_LONG).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("'username' length expected between 3 and 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .post("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPostWithFirstNameTooLong_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().username("test").firstName(TOO_LONG).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("'first_name' length may not exceed 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .post("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPostWithLastNameTooLong_shouldRespondWithStatusBadRequest() {
    UserDTO userDTO = UserDTO.builder().username("test").lastName(TOO_LONG).build();

    ExceptionResponse badRequestResponse =
        ExceptionResponse.builder()
            .status(BAD_REQUEST)
            .message("'last_name' length may not exceed 255")
            .build();

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(userDTO))
        .post("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(BAD_REQUEST.value())
        .body(is(equalTo(JsonEncoder.encodeToString(badRequestResponse))));
  }

  @Test
  public void testPostWithValidUser_shouldRespondWithStatusCreatedAndCreatedUser() {
    UserDTO requestUserDTO =
        UserDTO.builder().username("test").firstName("fred").lastName("george").build();

    User user = User.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    UserDTO expected =
        UserDTO.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    when(userCreator.create(requestUserDTO)).thenReturn(user);

    given()
        .accept(JSON)
        .contentType(JSON)
        .body(JsonEncoder.encodeToString(requestUserDTO))
        .post("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(CREATED.value())
        .body(is(equalTo(JsonEncoder.encodeToString(expected))));

    UserDTO actual = userController.post(requestUserDTO);

    assertThat(actual, is(equalTo(expected)));
  }
}
