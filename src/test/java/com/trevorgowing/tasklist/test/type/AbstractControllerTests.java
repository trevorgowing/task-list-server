package com.trevorgowing.tasklist.test.type;

import com.trevorgowing.tasklist.common.exception.ControllerExceptionHandler;
import com.trevorgowing.tasklist.test.category.ControllerUnitTests;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.experimental.categories.Category;

@Category(ControllerUnitTests.class)
public abstract class AbstractControllerTests extends AbstractTests {

  protected abstract Object getController();

  @Before
  public void setup() {
    RestAssuredMockMvc.standaloneSetup(getController(), new ControllerExceptionHandler());
  }
}
