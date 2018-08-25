package com.trevorgowing.tasklist.user;

import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.trevorgowing.tasklist.common.exception.ExceptionResponse;
import com.trevorgowing.tasklist.test.encoder.JsonEncoder;
import com.trevorgowing.tasklist.test.type.AbstractSpringWebContextTests;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.EntityManager;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserApiTests extends AbstractSpringWebContextTests {

  @Autowired private EntityManager entityManager;

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
}
