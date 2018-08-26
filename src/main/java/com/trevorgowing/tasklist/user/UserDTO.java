package com.trevorgowing.tasklist.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trevorgowing.tasklist.common.validation.Create;
import com.trevorgowing.tasklist.common.validation.Modify;
import java.io.Serializable;
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
class UserDTO implements Serializable {

  private static final long serialVersionUID = -4544529682149894979L;

  // VM = Validation Message
  private static final String USERNAME_NOT_NULL_VM = "\'username\' is required";
  private static final String USERNAME_SIZE_VM = "\'username\' length expected between 3 and 255";
  private static final String FIRST_SIZE_VM = "\'first_name\' length may not exceed 255";
  private static final String LAST_SIZE_VM = "\'last_name\' length may not exceed 255";

  private Long id;

  @NotNull(
    message = USERNAME_NOT_NULL_VM,
    groups = {Create.class}
  )
  @Size(
    min = 3,
    max = 255,
    message = USERNAME_SIZE_VM,
    groups = {Create.class, Modify.class}
  )
  private String username;

  @JsonProperty("first_name")
  @Size(
    max = 255,
    message = FIRST_SIZE_VM,
    groups = {Create.class, Modify.class}
  )
  private String firstName;

  @JsonProperty("last_name")
  @Size(
    max = 255,
    message = LAST_SIZE_VM,
    groups = {Create.class, Modify.class}
  )
  private String lastName;

  private UserDTO(User user) {
    this(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());
  }

  static UserDTO from(User user) {
    return new UserDTO(user);
  }
}
