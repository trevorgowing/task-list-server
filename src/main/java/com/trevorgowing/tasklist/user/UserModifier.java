package com.trevorgowing.tasklist.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserModifier {

  private final UserRepository userRepository;

  User replace(UserDTO userDTO) {
    User user =
        userRepository
            .findById(userDTO.getId())
            .orElseThrow(
                () ->
                    UserNotFoundException.causedBy(
                        String.format("User not found for id: \'%s\'", userDTO.getId())));

    user.replace(userDTO);

    try {
      return userRepository.save(user);
    } catch (DataIntegrityViolationException dive) {
      throw DuplicateUsernameException.causedBy(
          String.format("User with username: \'%s\' already exists", user.getUsername()));
    }
  }
}
