package com.trevorgowing.tasklist.task;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import java.time.LocalDateTime;
import org.junit.Test;

public class TaskDTOTests extends AbstractTests {

  @Test
  public void testFrom_shouldConstructWithCorrectState() {
    LocalDateTime date = LocalDateTime.now();

    Task task =
        Task.builder().id(1L).name("name").description("description").dateTime(date).build();

    TaskDTO expected =
        TaskDTO.builder().id(1L).name("name").description("description").dateTime(date).build();

    TaskDTO actual = TaskDTO.from(task);

    assertThat(actual, is(equalTo(expected)));
  }
}
