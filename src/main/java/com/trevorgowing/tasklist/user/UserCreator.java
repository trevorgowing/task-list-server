package com.trevorgowing.tasklist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserCreator {

  private final UserRepository userRepository;

  User create(UserDTO userDTO) {
    try {
      return userRepository.save(User.from(userDTO));
    } catch (DataIntegrityViolationException dive) {
      throw DuplicateUsernameException.causedBy(
          String.format("User with username: \'%s\' already exists", userDTO.getUsername()));
    }
  }
}
