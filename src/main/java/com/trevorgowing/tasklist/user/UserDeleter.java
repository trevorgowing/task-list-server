package com.trevorgowing.tasklist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserDeleter {

  private final UserRepository userRepository;

  void deleteById(Long id) {
    try {
      userRepository.deleteById(id);
    } catch (EmptyResultDataAccessException eredae) {
      throw UserNotFoundException.causedBy(String.format("User not found for id: %s", id));
    }
  }
}
