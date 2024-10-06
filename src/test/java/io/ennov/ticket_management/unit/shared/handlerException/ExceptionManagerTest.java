package io.ennov.ticket_management.unit.shared.handlerException;

import io.ennov.ticket_management.shared.ConflictAssignException;
import io.ennov.ticket_management.shared.handlerException.ApiResponse;
import io.ennov.ticket_management.shared.handlerException.ExceptionManager;
import io.ennov.ticket_management.ticket.TicketNotFoundException;
import io.ennov.ticket_management.user.EmailAlreadyExistsException;
import io.ennov.ticket_management.user.UserNotFoundException;
import io.ennov.ticket_management.user.UsernameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExceptionManagerTest {

    private ExceptionManager exceptionManager;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionManager = new ExceptionManager();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("test description");
    }

    @Nested
    @DisplayName("TicketNotFoundException Tests")
    class TicketNotFoundExceptionTests {
        @Test
        @DisplayName("Should handle TicketNotFoundException")
        void shouldHandleTicketNotFoundException() {
            // Given
            TicketNotFoundException ex = new TicketNotFoundException("Ticket not found");

            // When
            ResponseEntity<Object> response = exceptionManager.handleTicketNotFoundException(ex, webRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isEqualTo("Ticket not found");
        }
    }

    @Nested
    @DisplayName("UserNotFoundException Tests")
    class UserNotFoundExceptionTests {
        @Test
        @DisplayName("Should handle UserNotFoundException")
        void shouldHandleUserNotFoundException() {
            // Given
            UserNotFoundException ex = new UserNotFoundException("User not found");

            // When
            ResponseEntity<Object> response = exceptionManager.handleUserNotFoundException(ex, webRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(response.getBody()).isEqualTo("User not found");
        }
    }

    @Nested
    @DisplayName("UsernameAlreadyExistsException Tests")
    class UsernameAlreadyExistsExceptionTests {
        @Test
        @DisplayName("Should handle UsernameAlreadyExistsException")
        void shouldHandleUsernameAlreadyExistsException() {
            // Given
            UsernameAlreadyExistsException ex = new UsernameAlreadyExistsException("Username already exists");

            // When
            ResponseEntity<Object> response = exceptionManager.handleUsernameAlreadyExists(ex, webRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isEqualTo("Username already exists");
        }
    }

    @Nested
    @DisplayName("EmailAlreadyExistsException Tests")
    class EmailAlreadyExistsExceptionTests {
        @Test
        @DisplayName("Should handle EmailAlreadyExistsException")
        void shouldHandleEmailAlreadyExistsException() {
            // Given
            EmailAlreadyExistsException ex = new EmailAlreadyExistsException("Email already exists");

            // When
            ResponseEntity<Object> response = exceptionManager.handleEmailAlreadyExists(ex, webRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isEqualTo("Email already exists");
        }
    }

    @Nested
    @DisplayName("ConflictAssignException Tests")
    class ConflictAssignExceptionTests {
        @Test
        @DisplayName("Should handle ConflictAssignException")
        void shouldHandleConflictAssignException() {
            // Given
            ConflictAssignException ex = new ConflictAssignException("Ticket is already assigned to another user");

            // When
            ResponseEntity<Object> response = exceptionManager.handleConflictAssignException(ex, webRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isEqualTo("Ticket is already assigned to another user");
        }
    }

    @Nested
    @DisplayName("General Exception Tests")
    class GeneralExceptionTests {
        @Test
        @DisplayName("Should handle general exceptions")
        void shouldHandleGeneralExceptions() {
            // Given
            Exception ex = new RuntimeException("Unexpected error");

            // When
            ResponseEntity<Object> response = exceptionManager.handleAllExceptions(ex, webRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isInstanceOf(ApiResponse.class);

            ApiResponse apiResponse = (ApiResponse) response.getBody();
            assertThat(apiResponse.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
            assertThat(apiResponse.getSuccess()).isFalse();
            assertThat(apiResponse.getMessage()).isEqualTo("Unexpected error");
            assertThat(apiResponse.getDetails()).isEqualTo("test description");
        }

        @Test
        @DisplayName("Should handle exceptions with null message")
        void shouldHandleExceptionsWithNullMessage() {
            // Given
            Exception ex = new RuntimeException();

            // When
            ResponseEntity<Object> response = exceptionManager.handleAllExceptions(ex, webRequest);

            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isInstanceOf(ApiResponse.class);

            ApiResponse apiResponse = (ApiResponse) response.getBody();
            assertThat(apiResponse.getMessage()).isEqualTo("An unexpected error occurred");
        }
    }
}
