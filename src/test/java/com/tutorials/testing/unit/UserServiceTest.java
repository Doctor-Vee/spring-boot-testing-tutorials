package com.tutorials.testing.unit;

import com.tutorials.testing.exception.UserRegistrationException;
import com.tutorials.testing.model.User;
import com.tutorials.testing.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;



//    @BeforeEach
//    void setUp(){
//        userRepository = mock(UserRepository.class);
//        userService = new UserService(userRepository);
//    }

    @Test
    void shouldSaveUserSuccessfully(){
        User user = new User(null, "vee@gmail.com", "vee", "Vee");
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        given(userRepository.save(user)).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        User savedUser = userService.createUser(user);

        assertThat(savedUser).isNotNull();

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowErrorWhenUserWithExistingEmailIsSaved(){
        User user = new User(1L, "ovisco@gmail.com", "victor", "Victor");
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        assertThrows(UserRegistrationException.class, () -> {
            userService.createUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }
}
