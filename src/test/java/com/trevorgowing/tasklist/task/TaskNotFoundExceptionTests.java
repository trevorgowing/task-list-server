package com.trevorgowing.tasklist.task;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;

public class TaskNotFoundExceptionTests extends AbstractTests {

  @Test
  public void testCausedByWithMessage_shouldReturnTaskNotFoundExceptionWithCorrectState() {
    String message = "message";

    TaskNotFoundException actual = TaskNotFoundException.causedBy(message);

    assertThat(actual.getMessage(), is(equalTo(message)));
  }
}
