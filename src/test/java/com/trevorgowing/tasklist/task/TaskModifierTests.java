package com.trevorgowing.tasklist.task;

import static com.trevorgowing.tasklist.task.TaskMatcher.hasSameStateAsTask;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.trevorgowing.tasklist.common.exception.AccessDeniedException;
import com.trevorgowing.tasklist.test.type.AbstractTests;
import com.trevorgowing.tasklist.user.User;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import com.trevorgowing.tasklist.user.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TaskModifierTests extends AbstractTests {

  @Mock private UserRepository userRepository;
  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskModifier taskModifier;

  @Test(expected = UserNotFoundException.class)
  public void testModifyWithNoMatchingUser_shouldThrow() {
    Long userId = 1L;
    Long taskId = 2L;

    TaskDTO taskDTO = TaskDTO.builder().build();

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    taskModifier.modify(userId, taskId, taskDTO);
  }

  @Test(expected = TaskNotFoundException.class)
  public void testModifyWithNoMatchingTask_shouldThrow() {
    Long userId = 1L;
    Long taskId = 2L;

    TaskDTO taskDTO = TaskDTO.builder().build();

    when(userRepository.findById(userId))
        .thenReturn(Optional.of(User.builder().id(userId).build()));
    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    taskModifier.modify(userId, taskId, taskDTO);
  }

  @Test(expected = AccessDeniedException.class)
  public void testModifyWithMismatchingUsers_shouldThrow() {
    Long userId = 1L;
    Long taskId = 2L;

    TaskDTO taskDTO = TaskDTO.builder().build();

    User user = User.builder().id(userId).build();
    Task task = Task.builder().id(taskId).user(User.builder().id(3L).build()).build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

    taskModifier.modify(userId, taskId, taskDTO);
  }

  @Test
  public void testModify_shouldDelegateToTaskRepository() {
    Long userId = 1L;
    Long taskId = 2L;
    LocalDateTime past = LocalDateTime.now();
    LocalDateTime future = LocalDateTime.now().plusDays(1);

    TaskDTO taskDTO = TaskDTO.builder().name("current").description("new").dateTime(future).build();

    User user = User.builder().id(userId).build();
    Task task =
        Task.builder()
            .id(taskId)
            .user(user)
            .name("original")
            .description("old")
            .dateTime(past)
            .build();

    Task expected =
        Task.builder()
            .id(taskId)
            .user(user)
            .name("current")
            .description("new")
            .dateTime(future)
            .build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
    when(taskRepository.save(argThat(hasSameStateAsTask(expected)))).thenReturn(expected);

    Task actual = taskModifier.modify(userId, taskId, taskDTO);

    assertThat(actual, hasSameStateAsTask(expected));
  }
}
