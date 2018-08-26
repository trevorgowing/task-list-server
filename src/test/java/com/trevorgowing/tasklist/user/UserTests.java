package com.trevorgowing.tasklist.user;

import static com.trevorgowing.tasklist.test.matcher.StringContainsAllMatcher.containsStrings;
import static com.trevorgowing.tasklist.user.UserMatcher.hasSameStateAsUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasToString;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;

public class UserTests extends AbstractTests {

  @Test
  public void testFromWithUserDTO_shouldConstructWithCorrectState() {
    String username = "username";
    String firstName = "firstName";
    String lastName = "lastName";

    UserDTO userDTO =
        UserDTO.builder().username(username).firstName(firstName).lastName(lastName).build();

    User expected =
        User.builder().username(username).firstName(firstName).lastName(lastName).build();

    User actual = User.from(userDTO);

    assertThat(actual, hasSameStateAsUser(expected));
  }

  @Test
  public void testUpdateWithAllFieldsNull_shouldNotUpdateAnyFields() {
    UserDTO userDTO = UserDTO.builder().build();

    User user =
        User.builder().username("username").firstName("firstName").lastName("lastName").build();

    User expected =
        User.builder().username("username").firstName("firstName").lastName("lastName").build();

    user.update(userDTO);

    assertThat(user, hasSameStateAsUser(expected));
  }

  @Test
  public void testUpdateWithAllFieldsChanged_shouldUpdateAllFields() {
    UserDTO userDTO =
        UserDTO.builder().username("test").firstName("fred").lastName("george").build();

    User user =
        User.builder().username("username").firstName("firstName").lastName("lastName").build();

    User expected = User.builder().username("test").firstName("fred").lastName("george").build();

    user.update(userDTO);

    assertThat(user, hasSameStateAsUser(expected));
  }

  @Test
  public void testToString_shouldIncludeAllFields() {
    Long id = 1L;
    String username = "test";
    String firstName = "fred";
    String lastName = "george";

    User user =
        User.builder().id(id).username(username).firstName(firstName).lastName(lastName).build();

    assertThat(
        user.toString(),
        hasToString(
            containsStrings(
                "id", "1", "username", "test", "firstName", "fred", "lastName", "george")));
  }
}
