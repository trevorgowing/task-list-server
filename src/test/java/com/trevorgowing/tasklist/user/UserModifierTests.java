package com.trevorgowing.tasklist.user;

import static com.trevorgowing.tasklist.user.UserMatcher.hasSameStateAsUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import com.trevorgowing.tasklist.test.type.AbstractTests;
import java.util.Optional;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UserModifierTests extends AbstractTests {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserModifier userModifier;

  @Test(expected = UserNotFoundException.class)
  public void testReplaceWithNoMatchingUser_shouldThrow() {
    UserDTO userDTO =
        UserDTO.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    userModifier.replace(userDTO);
  }

  @Test
  public void
      testReplaceWithMatchingUser_shouldReplaceValuesAndDelegateToUserRepositoryAndReturnUser() {
    UserDTO userDTO =
        UserDTO.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    User existingUser =
        User.builder().id(1L).username("other").firstName("bob").lastName("lee").build();

    User expected =
        User.builder().id(1L).username("test").firstName("fred").lastName("george").build();

    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(argThat(hasSameStateAsUser(expected)))).thenReturn(expected);

    User actual = userModifier.replace(userDTO);

    assertThat(actual, hasSameStateAsUser(expected));
  }
}
