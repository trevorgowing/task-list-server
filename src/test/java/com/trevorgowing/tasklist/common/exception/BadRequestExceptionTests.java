package com.trevorgowing.tasklist.common.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;

public class BadRequestExceptionTests extends AbstractTests {

  @Test
  public void testCausedByWithMessage_shouldReturnBadRequestExceptionWithCorrectState() {
    String message = "message";
    BadRequestException expected = BadRequestException.builder().message(message).build();

    BadRequestException actual = BadRequestException.causedBy(message);

    assertThat(actual.getMessage(), is(equalTo(expected.getMessage())));
  }
}
