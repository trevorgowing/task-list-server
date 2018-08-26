package com.trevorgowing.tasklist.task;

import com.trevorgowing.tasklist.common.exception.AccessDeniedException;
import com.trevorgowing.tasklist.user.User;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import com.trevorgowing.tasklist.user.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class TaskModifier {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;

  Task modify(Long userId, Long taskId, TaskDTO taskDTO) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    UserNotFoundException.causedBy(
                        String.format("User not found for id: \'%s\'", userId)));

    Task task =
        taskRepository
            .findById(taskId)
            .orElseThrow(
                () ->
                    TaskNotFoundException.causedBy(
                        String.format("Task not found for id: \'%s\'", taskId)));

    if (!Objects.equals(user, task.getUser())) {
      throw AccessDeniedException.causedBy("Task does not belong to user");
    }

    task.modify(taskDTO);

    return taskRepository.save(task);
  }
}
