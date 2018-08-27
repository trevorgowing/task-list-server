package com.trevorgowing.tasklist.user;

import static javax.persistence.CascadeType.ALL;

import com.trevorgowing.tasklist.task.Task;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor
@Table(
  name = "users",
  uniqueConstraints = {@UniqueConstraint(name = "username_uidx", columnNames = "username")}
)
@EntityListeners(AuditingEntityListener.class)
public class User extends AbstractAuditable<User, Long> implements Serializable {

  private static final long serialVersionUID = 891397974680533600L;

  @NotNull
  @Basic(optional = false)
  @Size(min = 3, max = 255)
  @Column(name = "username", nullable = false)
  private String username;

  @Size(max = 255)
  @Column(name = "first_name")
  private String firstName;

  @Size(max = 255)
  @Column(name = "last_name")
  private String lastName;

  @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
  private Set<Task> tasks = new HashSet<>();

  @Builder
  @SuppressWarnings("unused")
  private User(Long id, String username, String firstName, String lastName) {
    setId(id);
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  private User(UserDTO userDTO) {
    this.username = userDTO.getUsername();
    this.firstName = userDTO.getFirstName();
    this.lastName = userDTO.getLastName();
  }

  static User from(UserDTO userDTO) {
    return new User(userDTO);
  }

  void update(UserDTO userDTO) {
    if (userDTO.getUsername() != null) {
      this.username = userDTO.getUsername();
    }
    if (userDTO.getFirstName() != null) {
      this.firstName = userDTO.getFirstName();
    }
    if (userDTO.getLastName() != null) {
      this.lastName = userDTO.getLastName();
    }
  }

  @Override
  public String toString() {
    final StringBuilder toStringBuilder = new StringBuilder(getClass().getSimpleName());
    toStringBuilder.append("{id='").append(getId()).append('\'');
    toStringBuilder.append(", username='").append(getUsername()).append('\'');
    toStringBuilder.append(", firstName='").append(getFirstName()).append('\'');
    toStringBuilder.append(", lastName='").append(getLastName()).append("\'}");
    return toStringBuilder.toString();
  }
}
