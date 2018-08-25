package com.trevorgowing.tasklist.user;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor
class TestUserFinder {

  private final EntityManager entityManager;

  User findByUsername(String username) {
    return entityManager
        .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
        .setParameter("username", username)
        .getSingleResult();
  }
}
