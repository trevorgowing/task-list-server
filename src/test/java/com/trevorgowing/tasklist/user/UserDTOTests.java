package com.trevorgowing.tasklist.user;

import static com.trevorgowing.tasklist.test.matcher.StringContainsAllMatcher.containsStrings;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;

public class UserDTOTests extends AbstractTests {

  @Test
  public void testFromWithUser_shouldConstructWithCorrectState() {
    Long id = 1L;
    String username = "username";
    String firstName = "firstName";
    String lastName = "lastName";

    User user =
        User.builder().id(id).username(username).firstName(firstName).lastName(lastName).build();

    UserDTO expected =
        UserDTO.builder().id(id).username(username).firstName(firstName).lastName(lastName).build();

    UserDTO actual = UserDTO.from(user);

    assertThat(actual, is(equalTo(expected)));
  }

  @Test
  public void testToString_shouldIncludeAllFields() {
    Long id = 1L;
    String username = "test";
    String firstName = "fred";
    String lastName = "george";

    UserDTO userDTO =
        UserDTO.builder().id(id).username(username).firstName(firstName).lastName(lastName).build();

    assertThat(
        userDTO.toString(),
        hasToString(
            containsStrings(
                "id", "1", "username", "test", "firstName", "fred", "lastName", "george")));
  }
}
