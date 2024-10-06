package io.ennov.ticket_management.unities.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ennov.ticket_management.controller.UserController;
import io.ennov.ticket_management.dto.UserDto;
import io.ennov.ticket_management.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

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
