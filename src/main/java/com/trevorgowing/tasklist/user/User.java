package com.trevorgowing.tasklist.user;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
  name = "users",
  uniqueConstraints = {@UniqueConstraint(name = "username_uidx", columnNames = "username")}
)
class User extends AbstractAuditable<User, Long> implements Serializable {

  private static final long serialVersionUID = 891397974680533600L;

  @NotNull
  @Basic(optional = false)
  @Size(min = 3, max = 255)
  @Column(name = "username", nullable = false)
  private String username;

  @Size(max = 255)
  @Column(name = "firstName")
  private String firstName;

  @Size(max = 255)
  @Column(name = "lastName")
  private String lastName;

  @Builder
  private User(Long id, String username, String firstName, String lastName) {
    setId(id);
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  static User from(UserDTO userDTO) {
    return new User(
        userDTO.getId(), userDTO.getUsername(), userDTO.getFirstName(), userDTO.getLastName());
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
