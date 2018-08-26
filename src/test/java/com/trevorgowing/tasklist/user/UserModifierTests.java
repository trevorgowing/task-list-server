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
  public void testModifyWithNoMatchingUser_shouldThrow() {
    Long id = 1L;

    UserDTO userDTO =
        UserDTO.builder().username("test").firstName("fred").lastName("george").build();

    when(userRepository.findById(id)).thenReturn(Optional.empty());

    userModifier.modify(id, userDTO);
  }

  @Test
  public void
      testModifyWithMatchingUser_shouldReplaceValuesAndDelegateToUserRepositoryAndReturnUser() {
    Long id = 1L;

    UserDTO userDTO =
        UserDTO.builder().username("test").firstName("fred").lastName("george").build();

    User existingUser =
        User.builder().id(id).username("other").firstName("bob").lastName("lee").build();

    User expected =
        User.builder().id(id).username("test").firstName("fred").lastName("george").build();

    when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(argThat(hasSameStateAsUser(expected)))).thenReturn(expected);

    User actual = userModifier.modify(id, userDTO);

    assertThat(actual, hasSameStateAsUser(expected));
  }
}
