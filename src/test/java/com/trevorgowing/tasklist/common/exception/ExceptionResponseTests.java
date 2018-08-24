package com.trevorgowing.tasklist.common.exception;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;

public class ExceptionResponseTests extends AbstractTests {

  @Test
  public void testFrom_shouldConstructExceptionResponse() {
    ExceptionResponse expectedExceptionResponse =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message("Internal Server Error")
            .build();

    ExceptionResponse actualExceptionResponse =
        ExceptionResponse.from(INTERNAL_SERVER_ERROR, "Internal Server Error");

    assertThat(actualExceptionResponse, is(equalTo(expectedExceptionResponse)));
  }

  @Test
  public void testEqualsWithNull_shouldNotBeEqual() {
    assertThat(ExceptionResponse.builder().status(OK).build(), is(not(equalTo(null))));
  }

  @Test
  public void testEqualsWithDifferentClass_shouldNotBeEqual() {
    assertThat(ExceptionResponse.builder().status(OK).build(), is(not(equalTo(new Object()))));
  }

  @Test
  public void testEqualsWithDifferentStatuses_shouldNotBeEqual() {
    ExceptionResponse exceptionResponseStatus400 =
        ExceptionResponse.builder().status(BAD_REQUEST).build();

    ExceptionResponse exceptionResponseStatus500 =
        ExceptionResponse.builder().status(INTERNAL_SERVER_ERROR).build();

    assertThat(exceptionResponseStatus400, is(not(equalTo(exceptionResponseStatus500))));
  }

  @Test
  public void testEqualsWithDifferentMessages_shouldNotBeEqual() {
    ExceptionResponse exceptionResponseBadRequest =
        ExceptionResponse.builder().status(BAD_REQUEST).message("Bad Request").build();

    ExceptionResponse exceptionResponseInternalServerError =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message("Internal Sever Error")
            .build();

    assertThat(exceptionResponseBadRequest, is(not(equalTo(exceptionResponseInternalServerError))));
  }

  @Test
  public void testEqualsWithEqualExceptionResponses_shouldBeEqual() {
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message("Internal Server Error")
            .build();

    ExceptionResponse equivalentExceptionResponse =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message("Internal Server Error")
            .build();

    assertThat(exceptionResponse, is(equalTo(equivalentExceptionResponse)));
  }

  @Test
  public void testHashcodeWithDifferentStatuses_shouldNotBeEqual() {
    ExceptionResponse exceptionResponseStatus400 =
        ExceptionResponse.builder().status(BAD_REQUEST).build();

    ExceptionResponse exceptionResponseStatus500 =
        ExceptionResponse.builder().status(INTERNAL_SERVER_ERROR).build();

    assertThat(exceptionResponseStatus400.hashCode(), is(not(equalTo(exceptionResponseStatus500))));
  }

  @Test
  public void testHashcodeWithDifferentMessages_shouldNotBeEqual() {
    ExceptionResponse exceptionResponseBadRequest =
        ExceptionResponse.builder().status(BAD_REQUEST).message("Bad Request").build();

    ExceptionResponse exceptionResponseInternalServerError =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message("Internal Sever Error")
            .build();

    assertThat(
        exceptionResponseBadRequest.hashCode(),
        is(not(equalTo(exceptionResponseInternalServerError.hashCode()))));
  }

  @Test
  public void testHashcodeWithEqualExceptionResponses_shouldBeEqual() {
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message("Internal Server Error")
            .build();

    ExceptionResponse equivalentExceptionResponse =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message("Internal Server Error")
            .build();

    assertThat(exceptionResponse.hashCode(), is(equalTo(equivalentExceptionResponse.hashCode())));
  }

  @Test
  public void testToString_ShouldContainStatusAndErrorAndMessage() {
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message("Internal Server Error")
            .build();

    assertThat(exceptionResponse, hasToString(containsString("status")));
    assertThat(exceptionResponse, hasToString(containsString("error")));
    assertThat(exceptionResponse, hasToString(containsString("message")));
  }
}
