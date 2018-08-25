package com.trevorgowing.tasklist.user;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;

public class UserDeleterTests extends AbstractTests {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserDeleter userDeleter;

  @Test(expected = UserNotFoundException.class)
  public void testDeleteByIdWithNoMatchingUser_shouldThrow() {
    Long id = 1L;

    doThrow(new EmptyResultDataAccessException(1)).when(userRepository).deleteById(id);

    userDeleter.deleteById(id);
  }

  @Test
  public void testDeleteByIdWithMatchingUser_shouldDoNothing() {
    Long id = 1L;

    doNothing().when(userRepository).deleteById(id);

    userDeleter.deleteById(id);
  }
}
