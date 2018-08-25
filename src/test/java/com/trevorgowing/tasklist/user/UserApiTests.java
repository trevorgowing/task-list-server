package com.trevorgowing.tasklist.user;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractSpringWebContextTests;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({TestUserFinder.class})
public class UserApiTests extends AbstractSpringWebContextTests {

  @Autowired private EntityManager entityManager;
  @Autowired private TestUserFinder testUserFinder;

  @Test
  public void testGetWithNoExistingUsers_shouldRespondWithStatusOkAndEmptyArray() {
    given()
        .accept(JSON)
        .get("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(Collections.emptyList()))));
  }

  @Test
  public void testGetWithExistingUsers_shouldRespondWithStatusOkAndUserArray() {
    User userOne = User.builder().username("one").firstName("one").lastName("one").build();
    entityManager.persist(userOne);
    UserDTO userOneDTO =
        UserDTO.builder()
            .id(userOne.getId())
            .username("one")
            .firstName("one")
            .lastName("one")
            .build();

    User userTwo = User.builder().username("two").firstName("two").lastName("two").build();
    entityManager.persist(userTwo);
    UserDTO userTwoDTO =
        UserDTO.builder()
            .id(userTwo.getId())
            .username("two")
            .firstName("two")
            .lastName("two")
            .build();

    Collection<UserDTO> users = Arrays.asList(userOneDTO, userTwoDTO);

    given()
        .accept(JSON)
        .get("/api/user")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(users))));
  }

  @Test
  public void testGetByIdWithNoMatchingUser_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    User userOne = User.builder().username("one").firstName("one").lastName("one").build();
    entityManager.persist(userOne);

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder().status(NOT_FOUND).message("User not found for id: 2").build();

    given()
        .accept(JSON)
        .get("/api/user/2")
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(NOT_FOUND.value())
        .body(is(equalTo(JsonEncoder.encodeToString(notFoundResponse))));
  }

  @Test
  public void testGetByIdWithMatchingUser_shouldRespondWithStatusOkAndUser() {
    User userOne = User.builder().username("one").firstName("one").lastName("one").build();
    entityManager.persist(userOne);
    UserDTO userOneDTO =
        UserDTO.builder()
            .id(userOne.getId())
            .username("one")
            .firstName("one")
            .lastName("one")
            .build();

    given()
        .accept(JSON)
        .get("/api/user/" + userOne.getId())
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(userOneDTO))));
  }

  @Test
  public void testPostWithValidUser_shouldRespondWithStatusCreatedAndUser() {
    String username = "username";

    UserDTO requestUser =
        UserDTO.builder().username(username).firstName("firstName").lastName("lastName").build();

    MockMvcResponse response =
        given()
            .accept(JSON)
            .contentType(JSON)
            .body(JsonEncoder.encodeToString(requestUser))
            .post("/api/user");

    User user = testUserFinder.findByUsername(username);

    UserDTO responseUserDTO =
        UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .build();

    response
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(CREATED.value())
        .body(is(equalTo(JsonEncoder.encodeToString(responseUserDTO))));
  }

  @Test
  public void testPutWithNoMatchingUser_shouldRespondWithStatusNotFoundAndExceptionResponse() {
    String username = "test";
    UserDTO requestUserDTO =
        UserDTO.builder().id(1L).username(username).firstName("fred").lastName("george").build();

    ExceptionResponse notFoundResponse =
        ExceptionResponse.builder()
            .status(NOT_FOUND)
            .message("User not found for id: \'1\'")
            .build();

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
  }

  @Test
  public void testPutWithValidUser_shouldRespondWithStatusOkAndUser() {
    User existingUser = User.builder().username("other").firstName("bob").lastName("lee").build();
    entityManager.persist(existingUser);

    String username = "test";
    UserDTO requestUserDTO =
        UserDTO.builder()
            .id(existingUser.getId())
            .username(username)
            .firstName("fred")
            .lastName("george")
            .build();

    MockMvcResponse response =
        given()
            .accept(JSON)
            .contentType(JSON)
            .body(JsonEncoder.encodeToString(requestUserDTO))
            .put("/api/user/" + existingUser.getId());

    User user = testUserFinder.findByUsername(username);

    UserDTO responseUserDTO =
        UserDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .build();

    response
        .then()
        .log()
        .ifValidationFails()
        .contentType(JSON)
        .statusCode(OK.value())
        .body(is(equalTo(JsonEncoder.encodeToString(responseUserDTO))));
  }
}
