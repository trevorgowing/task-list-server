package com.trevorgowing.tasklist.task;

import static javax.persistence.FetchType.LAZY;

import com.trevorgowing.tasklist.user.User;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractAuditable;

@Getter
@Entity
@NoArgsConstructor
@Table(
  name = "tasks",
  indexes = {@Index(name = "user_id_idx", columnList = "user_id")}
)
public class Task extends AbstractAuditable<User, Long> implements Serializable {

  private static final long serialVersionUID = 6863062100794258783L;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "user_id_fk"))
  private User user;

  @NotNull
  @Size(min = 1, max = 255)
  @Basic(optional = false)
  @Column(name = "name", nullable = false)
  private String name;

  @Size(min = 1, max = 255)
  @Column(name = "description")
  private String description;

  @NotNull
  @Basic(optional = false)
  @Column(name = "date_time")
  private LocalDateTime dateTime;

  @Builder
  @SuppressWarnings("unused")
  private Task(Long id, User user, String name, String description, LocalDateTime dateTime) {
    setId(id);
    this.user = user;
    this.name = name;
    this.description = description;
    this.dateTime = dateTime;
  }

  private Task(User user, String name, String description, LocalDateTime dateTime) {
    this.user = user;
    this.name = name;
    this.description = description;
    this.dateTime = dateTime;
  }

  public static Task from(User user, TaskDTO taskDTO) {
    return new Task(user, taskDTO.getName(), taskDTO.getDescription(), taskDTO.getDateTime());
  }

  public void modify(TaskDTO taskDTO) {
    if (taskDTO.getName() != null) {
      this.name = taskDTO.getName();
    }
    if (taskDTO.getDescription() != null) {
      this.description = taskDTO.getDescription();
    }
    if (taskDTO.getDateTime() != null) {
      this.dateTime = taskDTO.getDateTime();
    }
  }

  @Override
  public String toString() {
    final StringBuilder toStringBuilder = new StringBuilder(getClass().getSimpleName());
    toStringBuilder.append("{id=").append(getId());
    toStringBuilder.append(", user=").append(getUser());
    toStringBuilder.append(", name='").append(getName()).append('\'');
    toStringBuilder.append(", description='").append(getDescription()).append('\'');
    toStringBuilder.append(", dateTime=").append(getDateTime()).append("\'}");
    return toStringBuilder.toString();
  }
}
