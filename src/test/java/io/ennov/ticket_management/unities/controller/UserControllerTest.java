package io.ennov.ticket_management.unities.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ennov.ticket_management.controller.UserController;
import io.ennov.ticket_management.domain.StatusTicket;
import io.ennov.ticket_management.dto.TicketDto;
import io.ennov.ticket_management.dto.UserDto;
import io.ennov.ticket_management.exception.UserNotFoundException;
import io.ennov.ticket_management.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Create User Tests")
    class CreateUserTests {

        @Test
        @DisplayName("Should create a new user")
        void shouldCreateNewUser() throws Exception {
            // Given
            UserDto inputDto = new UserDto(null, "newuser", "newuser@gmail.com");
            UserDto outputDto = new UserDto(1L, "newuser", "newuser@gmail.com");

            when(userService.createUser(any(UserDto.class))).thenReturn(outputDto);

            // When & Then
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.username").value("newuser"))
                    .andExpect(jsonPath("$.email").value("newuser@gmail.com"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for invalid input")
        void shouldReturn400ForInvalidInput() throws Exception {
            // Given
            UserDto invalidDto = new UserDto(null, "", "invalid-email");

            // When & Then
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle UserService exception")
        void shouldHandleUserServiceException() throws Exception {
            // Given
            UserDto inputDto = new UserDto(null, "newuser", "newuser@example.com");

            when(userService.createUser(any(UserDto.class))).thenThrow(new RuntimeException("Service Error"));

            // When & Then
            mockMvc.perform(post("/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Get User Tickets Tests")
    class GetUserTicketsTests {

        @Test
        @DisplayName("Should return list of tickets for existing user")
        void shouldReturnListOfTicketsForExistingUser() throws Exception {
            // Given
            Long userId = 1L;
            List<TicketDto> tickets = Arrays.asList(
                    new TicketDto(1L, "Ticket 1", "Description 1", StatusTicket.PENDING, userId),
                    new TicketDto(2L, "Ticket 2", "Description 2", StatusTicket.DONE, userId)
            );
            when(userService.findTicketByUser(userId)).thenReturn(tickets);

            // When & Then
            mockMvc.perform(get("/users/{userId}/ticket", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].title").value("Ticket 1"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].title").value("Ticket 2"));
        }

        @Test
        @DisplayName("Should return empty list when user has no tickets")
        void shouldReturnEmptyListWhenUserHasNoTickets() throws Exception {
            // Given
            Long userId = 1L;
            when(userService.findTicketByUser(userId)).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/users/{userId}/ticket", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Should return 404 Not Found when user does not exist")
        void shouldReturn404WhenUserDoesNotExist() throws Exception {
            // Given
            Long userId = 1L;
            when(userService.findTicketByUser(userId)).thenThrow(new UserNotFoundException("User not found: " + userId));

            // When & Then
            mockMvc.perform(get("/users/{userId}/ticket", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Update User Tests")
    class UpdateUserTests {

        @Test
        @DisplayName("Should update user successfully")
        void shouldUpdateUserSuccessfully() throws Exception {
            // Given
            Long userId = 1L;
            UserDto inputDto = new UserDto(null, "updateduser", "updated@example.com");
            UserDto outputDto = new UserDto(userId, "updateduser", "updated@example.com");

            when(userService.modifyUser(eq(userId), any(UserDto.class))).thenReturn(outputDto);

            // When & Then
            mockMvc.perform(put("/users/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(userId))
                    .andExpect(jsonPath("$.username").value("updateduser"))
                    .andExpect(jsonPath("$.email").value("updated@example.com"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for invalid input")
        void shouldReturn400ForInvalidInput() throws Exception {
            // Given
            Long userId = 1L;
            UserDto invalidDto = new UserDto(null, "", "invalid-email");

            // When & Then
            mockMvc.perform(put("/users/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when user does not exist")
        void shouldReturn404WhenUserDoesNotExist() throws Exception {
            // Given
            Long userId = 999L;
            UserDto inputDto = new UserDto(null, "updateduser", "updated@example.com");

            when(userService.modifyUser(eq(userId), any(UserDto.class)))
                    .thenThrow(new UserNotFoundException("User not found: " + userId));

            // When & Then
            mockMvc.perform(put("/users/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Delete User Tests")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user successfully")
        void shouldDeleteUserSuccessfully() throws Exception {
            // Given
            Long userId = 1L;
            doNothing().when(userService).deleteUser(userId);

            // When & Then
            mockMvc.perform(delete("/users/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(userService).deleteUser(userId);
        }

        @Test
        @DisplayName("Should return 404 Not Found when user does not exist")
        void shouldReturn404WhenUserDoesNotExist() throws Exception {
            // Given
            Long userId = 999L;
            doThrow(new UserNotFoundException("User not found: " + userId))
                    .when(userService).deleteUser(userId);

            // When & Then
            mockMvc.perform(delete("/users/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(userService).deleteUser(userId);
        }
    }
}
