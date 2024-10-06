package io.ennov.ticket_management.unities.domains.mapper;

import io.ennov.ticket_management.domain.User;
import io.ennov.ticket_management.domain.mapper.UserMapper;
import io.ennov.ticket_management.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("Should map User to UserDto")
    void shouldMapUserToUserDto() {
        // Given
        User user = User.builder()
                .id(1L)
                .username("testUser")
                .email("test@gmail.com")
                .ticketList(new ArrayList<>())
                .build();

        // When
        UserDto userDto = userMapper.userToUserDto(user);

        // Then
        assertThat(userDto).isNotNull();
        assertThat(userDto.id()).isEqualTo(user.getId());
        assertThat(userDto.username()).isEqualTo(user.getUsername());
        assertThat(userDto.email()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Should handle null User")
    void shouldHandleNullUser() {
        // Given
        User user = null;

        // When
        UserDto userDto = userMapper.userToUserDto(user);

        // Then
        assertThat(userDto).isNull();
    }
}
