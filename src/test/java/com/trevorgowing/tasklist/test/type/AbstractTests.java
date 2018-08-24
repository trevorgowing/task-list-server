package com.trevorgowing.tasklist.test.type;

import com.trevorgowing.tasklist.test.category.ServiceUnitTests;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@Category(ServiceUnitTests.class)
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractTests {

  @Rule
  public TestRule watcher =
      new TestWatcher() {
        protected void starting(Description description) {
          System.out.println(description.getDisplayName());
        }
      };
}
