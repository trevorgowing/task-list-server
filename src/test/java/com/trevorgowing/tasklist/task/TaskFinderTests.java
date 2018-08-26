package com.trevorgowing.tasklist.task;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TaskFinderTests extends AbstractTests {

  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskFinder taskFinder;

  @Test
  public void testFindDTOsByUserId_shouldDelegateToTaskRepositoryAndReturnTasks() {
    Long userId = 3L;

    TaskDTO taskOneDTO = TaskDTO.builder().id(1L).name("one").build();
    TaskDTO taskTwoDTO = TaskDTO.builder().id(2L).name("two").build();

    when(taskRepository.findDTOsByUserId(userId)).thenReturn(Arrays.asList(taskOneDTO, taskTwoDTO));

    Collection<TaskDTO> actual = taskFinder.findDTOsByUserId(userId);

    assertThat(actual, hasItems(taskOneDTO, taskTwoDTO));
  }

  @Test(expected = TaskNotFoundException.class)
  public void testFindDTOByUserIdAndTaskIdWithNoMatchingTask_shouldThrow() {
    Long userId = 1L;
    Long taskId = 2L;

    when(taskRepository.findDTOByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.empty());

    taskFinder.findDTOByUserIdAndTaskId(userId, taskId);
  }

  @Test
  public void testFindDTOByUserIdAndTaskIdWithMatchingTask_shouldThrow() {
    Long userId = 1L;
    Long taskId = 2L;

    TaskDTO expected = TaskDTO.builder().id(taskId).build();

    when(taskRepository.findDTOByUserIdAndTaskId(userId, taskId)).thenReturn(Optional.of(expected));

    TaskDTO actual = taskFinder.findDTOByUserIdAndTaskId(userId, taskId);

    assertThat(actual, is(equalTo(expected)));
  }
}
