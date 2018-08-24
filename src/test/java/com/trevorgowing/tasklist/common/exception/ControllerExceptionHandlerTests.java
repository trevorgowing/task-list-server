package com.trevorgowing.tasklist.common.exception;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import java.util.Collections;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.server.MethodNotAllowedException;

public class ControllerExceptionHandlerTests extends AbstractTests {

  @Mock private WebRequest webRequest;

  @InjectMocks private ControllerExceptionHandler exceptionHandler;

  @Test
  public void testHandleUnhandledException_shouldReturnResponseEntityContainingExceptionResponse() {
    HttpClientErrorException exception = new HttpClientErrorException(UNAUTHORIZED);

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(INTERNAL_SERVER_ERROR)
            .message(exception.getMessage())
            .build();

    ResponseEntity<ExceptionResponse> expected =
        ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .contentType(APPLICATION_JSON_UTF8)
            .body(exceptionResponse);

    ResponseEntity<ExceptionResponse> actual = exceptionHandler.handleUnhandledException(exception);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testHandleExceptionInternalWithClientError_shouldReturnResponseEntity() {
    MethodNotAllowedException exception =
        new MethodNotAllowedException(GET, Collections.singletonList(POST));

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(METHOD_NOT_ALLOWED)
            .message(exception.getMessage())
            .build();

    ResponseEntity<Object> expected =
        ResponseEntity.status(METHOD_NOT_ALLOWED)
            .contentType(APPLICATION_JSON_UTF8)
            .body(exceptionResponse);

    ResponseEntity<Object> actual =
        exceptionHandler.handleExceptionInternal(
            exception, new Object(), new HttpHeaders(), METHOD_NOT_ALLOWED, webRequest);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testHandleExceptionInternalWithServerError_shouldReturnResponseEntity() {
    AsyncRequestTimeoutException exception = new AsyncRequestTimeoutException();

    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(SERVICE_UNAVAILABLE)
            .message(exception.getMessage())
            .build();

    ResponseEntity<Object> expected =
        ResponseEntity.status(SERVICE_UNAVAILABLE)
            .contentType(APPLICATION_JSON_UTF8)
            .body(exceptionResponse);

    ResponseEntity<Object> actual =
        exceptionHandler.handleExceptionInternal(
            exception, new Object(), new HttpHeaders(), SERVICE_UNAVAILABLE, webRequest);

    assertThat(actual, is(equalTo(expected)));
  }
}
