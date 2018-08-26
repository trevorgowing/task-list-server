package com.trevorgowing.tasklist.task;

import com.trevorgowing.tasklist.user.User;
import com.trevorgowing.tasklist.user.UserNotFoundException;
import com.trevorgowing.tasklist.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class TaskCreator {

  private final UserRepository userRepository;
  private final TaskRepository taskRepository;

  public Task create(Long userId, TaskDTO taskDTO) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () ->
                    UserNotFoundException.causedBy(
                        String.format("User not found for id: \'%s\'", userId)));

    return taskRepository.save(Task.from(user, taskDTO));
  }
}
