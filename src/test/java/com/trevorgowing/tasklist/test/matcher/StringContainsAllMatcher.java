package com.trevorgowing.tasklist.test.matcher;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RequiredArgsConstructor
public class StringContainsAllMatcher extends TypeSafeMatcher<String> {

  private final String[] expected;

  public static StringContainsAllMatcher containsStrings(String... expected) {
    return new StringContainsAllMatcher(expected);
  }

  @Override
  protected boolean matchesSafely(String string) {
    for (String expectedString : expected) {
      if (!string.contains(expectedString)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A string containing: " + Arrays.toString(expected));
  }
}
