package com.trevorgowing.tasklist.task;

import static com.trevorgowing.tasklist.user.UserMatcher.hasSameStateAsUser;
import static com.trevorgowing.tasklist.user.UserMatcher.printUser;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RequiredArgsConstructor
class TaskMatcher extends TypeSafeMatcher<Task> {

  private final Task expected;

  static TaskMatcher hasSameStateAsTask(Task expected) {
    return new TaskMatcher(expected);
  }

  @Override
  protected boolean matchesSafely(Task actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getDescription(), expected.getDescription())
        && Objects.equals(actual.getDateTime(), expected.getDateTime())
        && (Objects.equals(actual.getUser(), expected.getUser())
            || hasSameStateAsUser(expected.getUser()).matches(actual.getUser()));
  }

  @Override
  protected void describeMismatchSafely(Task actual, Description description) {
    printTask(actual, description);
  }

  @Override
  public void describeTo(Description description) {
    printTask(expected, description);
  }

  private static void printTask(Task task, Description description) {
    description.appendText("Task { id: ").appendValue(task.getId()).appendText(", user: ");
    if (task.getUser() == null) {
      description.appendText("null");
    } else {
      printUser(task.getUser(), description);
    }
    description
        .appendText(", name: ")
        .appendValue(task.getName())
        .appendText(", description: ")
        .appendValue(task.getDescription())
        .appendText(", dateTime: ")
        .appendValue(task.getDateTime());
  }
}
