package io.ennov.ticket_management.unities.dto;

import io.ennov.ticket_management.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserDtoTest {

    @Test
    @DisplayName("Should create UserDto with all fields")
    void shouldCreateUserDtoWithAllFields() {
        // Given
        Long id = 1L;
        String username = "testUser";
        String email = "test@gmail.com";

        // When
        UserDto userDto = new UserDto(id, username, email);

        // Then
        assertThat(userDto.id()).isEqualTo(id);
        assertThat(userDto.username()).isEqualTo(username);
        assertThat(userDto.email()).isEqualTo(email);
    }

    @Test
    @DisplayName("Should create UserDto with null fields")
    void shouldCreateUserDtoWithNullFields() {
        // Given/When
        UserDto userDto = new UserDto(null, null, null);

        // Then
        assertThat(userDto.id()).isNull();
        assertThat(userDto.username()).isNull();
        assertThat(userDto.email()).isNull();
    }

    @Test
    @DisplayName("Should have correct toString representation")
    void shouldHaveCorrectToStringRepresentation() {
        // Given
        UserDto userDto = new UserDto(1L, "testUser", "test@gmail.com");

        // When
        String toStringResult = userDto.toString();

        // Then
        assertThat(toStringResult).contains("UserDto")
                .contains("id=1")
                .contains("username=testUser")
                .contains("email=test@gmail.com");
    }

    @Test
    @DisplayName("Should be equal for same values")
    void shouldBeEqualForSameValues() {
        // Given
        UserDto userDto1 = new UserDto(1L, "testUser", "test@gmail.com");
        UserDto userDto2 = new UserDto(1L, "testUser", "test@gmail.com");

        // When/Then
        assertThat(userDto1).isEqualTo(userDto2);
        assertThat(userDto1.hashCode()).isEqualTo(userDto2.hashCode());
    }
}
