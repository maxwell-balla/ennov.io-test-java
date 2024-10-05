package io.ennov.ticket_management.dto;

import jakarta.validation.constraints.Email;

public record UserDto(
        Long id,
        String username,
        @Email(message = "Email should be valid")
        String email
) {
        public UserDto(String username, String email) {
                this(null, username, email);
        }
}