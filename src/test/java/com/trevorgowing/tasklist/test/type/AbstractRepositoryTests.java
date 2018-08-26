package com.trevorgowing.tasklist.test.type;

import com.trevorgowing.tasklist.test.category.RepositoryIntegrationTests;
import javax.persistence.EntityManager;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Category(RepositoryIntegrationTests.class)
public abstract class AbstractRepositoryTests extends AbstractSpringTests {

  @Autowired protected EntityManager entityManager;
}
