package com.trevorgowing.tasklist.user;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
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

public class UserControllerGetTests extends AbstractControllerTests {

  @Mock private UserFinder userFinder;

  @InjectMocks private UserController userController;

  @Override
  protected Object getController() {
    return userController;
  }

  @Test
  public void testGetWithNoExistingUsers_shouldDelegateToUserFinderAndRespondWithStatusOk() {
    Collection<UserDTO> emptyCollection = Collections.emptyList();

    when(userFinder.findDTOs()).thenReturn(emptyCollection);

    given()
        .accept(JSON)
        .get("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(emptyCollection))));

    Collection<UserDTO> actual = userController.get();

    assertThat(actual, is(empty()));
  }

  @Test
  public void testGetWithExistingUsers_shouldDelegateToUserFinderAndRespondWithStatusOk() {
    UserDTO userOneDTO = UserDTO.builder().build();
    UserDTO userTwoDTO = UserDTO.builder().build();

    Collection<UserDTO> users = Arrays.asList(userOneDTO, userTwoDTO);

    when(userFinder.findDTOs()).thenReturn(users);

    given()
        .accept(JSON)
        .get("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(users))));

    Collection<UserDTO> actual = userController.get();

    assertThat(actual, hasItems(userOneDTO, userTwoDTO));
  }

  @Test(expected = UserNotFoundException.class)
  public void testGetByIdWithNoMatchingUser_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    String message = "User not found for id: 1";

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message(message).build();

    when(userFinder.findDTOById(1L))
        .thenThrow(UserNotFoundException.builder().message(message).build());

    given()
        .accept(JSON)
        .get("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));

    userController.get(1L);
  }

  @Test
  public void
      testGetByIdWithMatchingUser_shouldDelegateToUserFinderAndResponseWithStatusOkAndUser() {
    Long id = 1L;
    UserDTO expected = UserDTO.builder().id(id).username("username").build();

    when(userFinder.findDTOById(id)).thenReturn(expected);

    given()
        .accept(JSON)
        .get("/api/user/1")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(expected))));

    UserDTO actual = userController.get(id);

    assertThat(actual, is(equalTo(expected)));
  }
}
