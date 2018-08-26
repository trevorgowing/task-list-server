package com.trevorgowing.tasklist.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trevorgowing.tasklist.common.validation.Create;
import com.trevorgowing.tasklist.common.validation.Modify;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class TaskDTO implements Serializable {

  private static final long serialVersionUID = 4401900075943279833L;

  private static final String NAME_NOT_NULL_VM = "\'name\' is required";
  private static final String NAME_SIZE_VM = "\'name\' length expected between 1 and 255";
  private static final String DESCRIPTION_SIZE_VM =
      "\'description\' length expected between 1 and 255";
  private static final String DATE_TIME_VM = "\'date_time\' is required";

  private Long id;

  @NotNull(
    message = NAME_NOT_NULL_VM,
    groups = {
      Create.class,
    }
  )
  @Size(
    min = 1,
    max = 255,
    message = NAME_SIZE_VM,
    groups = {Create.class, Modify.class}
  )
  private String name;

  @Size(
    min = 1,
    max = 255,
    message = DESCRIPTION_SIZE_VM,
    groups = {Create.class, Modify.class}
  )
  private String description;

  @JsonProperty("date_time")
  @NotNull(
    message = DATE_TIME_VM,
    groups = {Create.class}
  )
  private LocalDateTime dateTime;

  /** Used by constructor queries in {@link TaskRepository}. */
  public TaskDTO(Task task) {
    this(task.getId(), task.getName(), task.getDescription(), task.getDateTime());
  }

  static TaskDTO from(Task task) {
    return new TaskDTO(task);
  }
}
