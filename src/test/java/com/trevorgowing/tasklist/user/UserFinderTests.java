package com.trevorgowing.tasklist.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserFinderTests extends AbstractTests {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserFinder userFinder;

  @Test
  public void testFindDTOs_shouldDelegateToUserRepositoryAndCollectDTOs() {
    User userOne = User.builder().build();
    UserDTO userOneDTO = UserDTO.builder().build();
    User userTwo = User.builder().build();
    UserDTO userTwoDTO = UserDTO.builder().build();

    when(userRepository.findAll()).thenReturn(Arrays.asList(userOne, userTwo));

    Collection<UserDTO> actual = userFinder.findDTOs();

    assertThat(actual, hasItems(userOneDTO, userTwoDTO));
  }

  @Test(expected = UserNotFoundException.class)
  public void testFindDTOByIdWithNoExistingUser_shouldThrow() {
    Long id = 1L;

    when(userRepository.findById(id)).thenReturn(Optional.empty());

    userFinder.findDTOById(id);
  }

  @Test
  public void testFindDTOByIdWithExistingUser_shouldDelegateToUserRepositoryAndReturnDTO() {
    Long id = 1L;

    User user = User.builder().id(id).build();
    UserDTO expected = UserDTO.builder().id(id).build();

    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    UserDTO actual = userFinder.findDTOById(id);

    assertThat(actual, is(equalTo(expected)));
  }
}
