package io.ennov.ticket_management.user;

import jakarta.validation.constraints.Email;

public record UserDto(
        Long id,
        String username,
        @Email(message = "Email should be valid")
        String email
) {
}