package com.trevorgowing.tasklist.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;

public class DuplicateUsernameExceptionTests extends AbstractTests {

  @Test
  public void testCausedByWithMessage_shouldConstructWithTheCorrectState() {
    String message = "message";

    DuplicateUsernameException actual = DuplicateUsernameException.causedBy(message);

    assertThat(actual.getMessage(), is(equalTo(message)));
  }
}
