package com.trevorgowing.tasklist.user;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserFinder {

  private final UserRepository userRepository;

  Collection<UserDTO> findDTOs() {
    return userRepository.findAll().stream().map(UserDTO::from).collect(Collectors.toList());
  }

  UserDTO findDTOById(Long id) {
    return userRepository
        .findById(id)
        .map(UserDTO::from)
        .orElseThrow(
            () -> UserNotFoundException.causedBy(String.format("User not found for id: %s", id)));
  }
}
