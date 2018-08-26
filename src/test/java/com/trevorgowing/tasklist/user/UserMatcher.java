package com.trevorgowing.tasklist.user;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RequiredArgsConstructor
public class UserMatcher extends TypeSafeMatcher<User> {

  private final User expected;

  public static UserMatcher hasSameStateAsUser(User expected) {
    return new UserMatcher(expected);
  }

  @Override
  protected boolean matchesSafely(User actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getUsername(), expected.getUsername())
        && Objects.equals(actual.getFirstName(), expected.getFirstName())
        && Objects.equals(actual.getLastName(), expected.getLastName());
  }

  @Override
  protected void describeMismatchSafely(User actual, Description description) {
    printUser(actual, description);
  }

  @Override
  public void describeTo(Description description) {
    printUser(expected, description);
  }

  public static void printUser(User user, Description description) {
    description
        .appendText("User { id: ")
        .appendValue(user.getId())
        .appendText(", username: ")
        .appendValue(user.getUsername())
        .appendText(", firsName: ")
        .appendValue(user.getFirstName())
        .appendText(", lastName: ")
        .appendValue(user.getLastName())
        .appendText(" }");
  }
}
