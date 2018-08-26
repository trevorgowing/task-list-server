package com.trevorgowing.tasklist.task;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.trevorgowing.tasklist.common.exception.AccessDeniedException;
import com.trevorgowing.tasklist.test.type.AbstractTests;
import com.trevorgowing.tasklist.user.User;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import com.trevorgowing.tasklist.user.UserRepository;
import java.util.Optional;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TaskDeleterTests extends AbstractTests {

  @Mock private UserRepository userRepository;
  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskDeleter taskDeleter;

  @Test(expected = UserNotFoundException.class)
  public void testDeleteByUserIdAndTaskIdWithNoMatchingUser_shouldThrow() {
    Long userId = 1L;
    Long taskId = 2L;

    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    taskDeleter.deleteByUserIdAndTaskId(userId, taskId);
  }

  @Test(expected = TaskNotFoundException.class)
  public void testDeleteByUserIdAndTaskIdWithNoMatchingTask_shouldThrow() {
    Long userId = 1L;
    Long taskId = 2L;

    when(userRepository.findById(userId))
        .thenReturn(Optional.of(User.builder().id(userId).build()));
    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    taskDeleter.deleteByUserIdAndTaskId(userId, taskId);
  }

  @Test(expected = AccessDeniedException.class)
  public void testDeleteByUserIdAndTaskIdWithMismatchingUsers_shouldThrow() {
    Long userId = 1L;
    Long taskId = 2L;

    User user = User.builder().id(userId).build();
    Task task = Task.builder().id(taskId).user(User.builder().id(3L).build()).build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

    taskDeleter.deleteByUserIdAndTaskId(userId, taskId);
  }

  @Test
  public void testDeleteByUserIdAndTaskId_shouldDelegateToTaskRepository() {
    Long userId = 1L;
    Long taskId = 2L;

    User user = User.builder().id(userId).build();
    Task task = Task.builder().id(taskId).user(user).build();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
    doNothing().when(taskRepository).delete(task);

    taskDeleter.deleteByUserIdAndTaskId(userId, taskId);

    verify(taskRepository).delete(task);
  }
}
