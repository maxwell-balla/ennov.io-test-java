package io.ennov.ticket_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserDto(
        Long id,

        @NotNull(message = "Username should be present")
        String username,

        @Email(message = "Email should be valid")
        String email
) {
}
