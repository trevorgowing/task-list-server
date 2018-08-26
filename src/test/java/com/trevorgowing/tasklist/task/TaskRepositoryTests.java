package com.trevorgowing.tasklist.task;

import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.tasklist.test.type.AbstractRepositoryTests;
import com.trevorgowing.tasklist.user.User;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskRepositoryTests extends AbstractRepositoryTests {

  @Autowired private TaskRepository taskRepository;

  @Test
  public void testFindDTOsByUserIdWithNotExistingTasks_shouldReturnEmptyCollection() {
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    User anotherUser = User.builder().username("anotherUser").build();
    entityManager.persist(anotherUser);
    entityManager.persist(
        Task.builder().user(anotherUser).name("anotherTask").dateTime(date).build());

    Collection<TaskDTO> actual = taskRepository.findDTOsByUserId(user.getId());

    assertThat(actual, is(empty()));
  }

  @Test
  public void testFindDTOsBYUserIdWithExistingTasks_shouldReturnTasks() {
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);

    Task taskOne = Task.builder().user(user).name("one").dateTime(date).build();
    entityManager.persist(taskOne);
    TaskDTO taskOneDTO = TaskDTO.builder().id(taskOne.getId()).name("one").dateTime(date).build();
    Task taskTwo = Task.builder().user(user).name("two").dateTime(date).build();
    entityManager.persist(taskTwo);
    TaskDTO taskTwoDTO = TaskDTO.builder().id(taskTwo.getId()).name("two").dateTime(date).build();

    User anotherUser = User.builder().username("anotherUser").build();
    entityManager.persist(anotherUser);
    entityManager.persist(Task.builder().user(user).name("anotherTask").dateTime(date).build());

    Collection<TaskDTO> actual = taskRepository.findDTOsByUserId(user.getId());

    assertThat(actual, hasItems(taskOneDTO, taskTwoDTO));
  }

  @Test
  public void testFindDTOByUserIdAndTaskIdWithNoMatchingTask_shouldReturnEmptyOptional() {
    User user = User.builder().username("user").build();
    entityManager.persist(user);

    User anotherUser = User.builder().username("anotherUser").build();
    entityManager.persist(anotherUser);
    Task task = Task.builder().user(anotherUser).name("task").dateTime(LocalDateTime.now()).build();
    entityManager.persist(task);

    Optional<TaskDTO> actual = taskRepository.findDTOByUserIdAndTaskId(user.getId(), 1000L);

    assertThat(actual, is(emptyOptional()));
  }

  @Test
  public void testFindDTOByUserIdAndTaskIdWithMatchingTask_shouldReturnTaskDTO() {
    LocalDateTime date = LocalDateTime.now();

    User user = User.builder().username("user").build();
    entityManager.persist(user);
    Task task = Task.builder().user(user).name("task").dateTime(date).build();
    entityManager.persist(task);
    TaskDTO expected = TaskDTO.builder().id(task.getId()).name("task").dateTime(date).build();

    Task anotherTask = Task.builder().user(user).name("anotherTask").dateTime(date).build();
    entityManager.persist(anotherTask);

    Optional<TaskDTO> actual = taskRepository.findDTOByUserIdAndTaskId(user.getId(), task.getId());

    assertThat(actual, is(optionalWithValue(equalTo(expected))));
  }
}
