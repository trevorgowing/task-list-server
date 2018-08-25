package com.trevorgowing.tasklist.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
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

  private Long id;
  private String username;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  static UserDTO from(User user) {
    return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName());
  }
}
