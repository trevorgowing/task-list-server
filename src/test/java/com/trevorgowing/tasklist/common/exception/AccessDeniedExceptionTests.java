package com.trevorgowing.tasklist.common.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;

public class AccessDeniedExceptionTests extends AbstractTests {

  @Test
  public void testCausedByWithMessage_shouldReturnAccessDeniedExceptionWithCorrectState() {
    String message = "message";
    AccessDeniedException expected = AccessDeniedException.builder().message(message).build();

    AccessDeniedException actual = AccessDeniedException.causedBy(message);

    assertThat(actual.getMessage(), is(equalTo(expected.getMessage())));
  }
}
