package com.trevorgowing.tasklist.task;

import static com.trevorgowing.tasklist.task.TaskMatcher.hasSameStateAsTask;
import static com.trevorgowing.tasklist.test.matcher.StringContainsAllMatcher.containsStrings;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import com.trevorgowing.tasklist.user.User;
import java.time.LocalDateTime;
import org.junit.Test;

public class TaskTests extends AbstractTests {

  @Test
  public void testFrom_shouldConstructWithCorrectState() {
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().id(1L).username("user").build();

    TaskDTO taskDTO =
        TaskDTO.builder().name("name").description("description").dateTime(date).build();

    Task expected =
        Task.builder().user(user).name("name").description("description").dateTime(date).build();

    Task actual = Task.from(user, taskDTO);

    assertThat(actual, hasSameStateAsTask(expected));
  }

  @Test
  public void testModifyWithAllFieldsNull_shouldNotModifyAnyFields() {
    LocalDateTime initialDate = LocalDateTime.now();

    TaskDTO taskDTO = TaskDTO.builder().build();

    Task task = Task.builder().name("test").description("test a lot").dateTime(initialDate).build();

    Task expected =
        Task.builder().name("test").description("test a lot").dateTime(initialDate).build();

    task.modify(taskDTO);

    assertThat(task, hasSameStateAsTask(expected));
  }

  @Test
  public void testModifyWithAllFieldsModified_shouldModifyAllFields() {
    LocalDateTime initialDate = LocalDateTime.now();
    LocalDateTime newDate = LocalDateTime.now().plusDays(1);

    TaskDTO taskDTO =
        TaskDTO.builder().name("code").description("code a lot").dateTime(newDate).build();

    Task task = Task.builder().name("test").description("test a lot").dateTime(initialDate).build();

    Task expected = Task.builder().name("code").description("code a lot").dateTime(newDate).build();

    task.modify(taskDTO);

    assertThat(task, hasSameStateAsTask(expected));
  }

  @Test
  public void testToString_shouldIncludeAllFields() {
    LocalDateTime date = LocalDateTime.now();

    Task task =
        Task.builder()
            .id(1L)
            .user(User.builder().id(2L).username("bob").build())
            .name("code")
            .description("test a lot")
            .dateTime(date)
            .build();

    assertThat(
        task.toString(),
        hasToString(
            containsStrings(
                "id",
                "1",
                "2",
                "username",
                "bob",
                "name",
                "code",
                "description",
                "test a lot",
                "dateTime",
                date.toString())));
  }
}
