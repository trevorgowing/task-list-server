package com.trevorgowing.tasklist.task;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class TaskFinder {

  private final TaskRepository taskRepository;

  Collection<TaskDTO> findDTOsByUserId(Long userId) {
    return taskRepository.findDTOsByUserId(userId);
  }

  TaskDTO findDTOByUserIdAndTaskId(Long userId, Long taskId) {
    return taskRepository
        .findDTOByUserIdAndTaskId(userId, taskId)
        .orElseThrow(
            () ->
                TaskNotFoundException.causedBy(
                    String.format(
                        "Task not found for userId: \'%s\' and taskId: \'%s\'", userId, taskId)));
  }
}
