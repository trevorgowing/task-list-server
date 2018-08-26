package com.trevorgowing.tasklist.task;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
class TestTaskFinder {

  private final EntityManager entityManager;

  Task findByName(String name) {
    return entityManager
        .createQuery("SELECT t FROM Task t WHERE t.name = :name", Task.class)
        .setParameter("name", name)
        .getSingleResult();
  }
}
