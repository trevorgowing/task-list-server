package com.trevorgowing.tasklist.user;

import static com.trevorgowing.tasklist.user.UserMatcher.hasSameStateAsUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

public class UserCreatorTests extends AbstractTests {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserCreator userCreator;

  @Test(expected = DuplicateUsernameException.class)
  public void testCreateWithDuplicateUsername_shouldThrow() {
    String username = "test";

    UserDTO userDTO = UserDTO.builder().username(username).build();

    User user = User.builder().username(username).build();

    when(userRepository.save(argThat(hasSameStateAsUser(user))))
        .thenThrow(new DataIntegrityViolationException("Duplicate username"));

    userCreator.create(userDTO);
  }

  @Test
  public void testCreate_shouldDelegateToUserRepositoryAndReturnPersistedUser() {
    UserDTO userDTO = UserDTO.builder().username("test").build();

    User transientUser = User.builder().username("test").build();

    User expected = User.builder().id(1L).username("test").build();

    when(userRepository.save(argThat(hasSameStateAsUser(transientUser)))).thenReturn(expected);

    User actual = userCreator.create(userDTO);

    assertThat(actual, is(equalTo(expected)));
  }
}
