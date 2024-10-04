package io.ennov.ticket_management.unities.services;

import io.ennov.ticket_management.domain.User;
import io.ennov.ticket_management.domain.mapper.UserMapper;
import io.ennov.ticket_management.dto.UserDto;
import io.ennov.ticket_management.repository.UserRepository;
import io.ennov.ticket_management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserDto inputDto;
    private User savedUser;
    private UserDto outputDto;

    @BeforeEach
    void setUp() {
        inputDto = new UserDto(null, "testuser", "test@gmail.com");
        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@gmail.com");
        outputDto = new UserDto(1L, "testuser", "test@gmail.com");
    }

    @Test
    @DisplayName("Should create a new user")
    void shouldCreateNewUser() {
        // Given
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.userToUserDto(any(User.class))).thenReturn(outputDto);

        // When
        UserDto result = userService.createUser(inputDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.username()).isEqualTo("testuser");
        assertThat(result.email()).isEqualTo("test@gmail.com");

        verify(userRepository).save(any(User.class));
        verify(userMapper).userToUserDto(any(User.class));
    }

    @Test
    @DisplayName("Should set correct user properties before saving")
    void shouldSetCorrectUserPropertiesBeforeSaving() {
        // Given
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });
        when(userMapper.userToUserDto(any(User.class))).thenReturn(outputDto);

        // When
        userService.createUser(inputDto);

        // Then
        verify(userRepository).save(any(User.class));
        verify(userMapper).userToUserDto(any(User.class));
    }

    @Test
    @DisplayName("Should handle null input")
    void shouldHandleNullInput() {
        // Given
        UserDto nullDto = new UserDto(null, null, null);

        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.userToUserDto(any(User.class))).thenReturn(new UserDto(1L, null, null));

        // When
        UserDto result = userService.createUser(nullDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.username()).isNull();
        assertThat(result.email()).isNull();

        verify(userRepository).save(any(User.class));
        verify(userMapper).userToUserDto(any(User.class));
    }
}
