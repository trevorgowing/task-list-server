package com.trevorgowing.tasklist.task;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface TaskRepository extends JpaRepository<Task, Long> {

  @Query(
      "SELECT NEW com.trevorgowing.tasklist.task.TaskDTO(t)" + "FROM Task t WHERE t.user.id = ?1")
  Collection<TaskDTO> findDTOsByUserId(Long userId);

  @Query(
      "SELECT NEW com.trevorgowing.tasklist.task.TaskDTO(t) "
          + "FROM Task t WHERE t.user.id = ?1 AND t.id = ?2")
  Optional<TaskDTO> findDTOByUserIdAndTaskId(Long userId, Long taskId);
}
