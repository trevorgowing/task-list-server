package com.trevorgowing.tasklist.task;

import static com.trevorgowing.tasklist.task.TaskMatcher.hasSameStateAsTask;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import com.trevorgowing.tasklist.user.User;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import com.trevorgowing.tasklist.user.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TaskCreatorTests extends AbstractTests {

  @Mock private UserRepository userRepository;
  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskCreator taskCreator;

  @Test(expected = UserNotFoundException.class)
  public void testCreateWithNoMatchingUser_shouldThrow() {
    Long userId = 1L;

    TaskDTO taskDTO = TaskDTO.builder().build();

    when(userRepository.findById(userId))
        .thenThrow(UserNotFoundException.builder().message("User not found for id: \'1\'").build());

    taskCreator.create(userId, taskDTO);
  }

  @Test
  public void testCreateWithMatchingUser_shouldDelegateToTaskRepositoryAndReturnCreatedTask() {
    Long userId = 1L;
    User user = User.builder().id(userId).build();

    LocalDateTime date = LocalDateTime.now();

    TaskDTO taskDTO = TaskDTO.builder().name("task").dateTime(date).build();

    Task task = Task.builder().user(user).name("task").dateTime(date).build();

    Task expected = Task.builder().id(2L).user(user).name("task").dateTime(date).build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(taskRepository.save(argThat(hasSameStateAsTask(task)))).thenReturn(expected);

    Task actual = taskCreator.create(userId, taskDTO);

    assertThat(actual, hasSameStateAsTask(expected));
  }
}
