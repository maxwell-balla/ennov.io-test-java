package io.ennov.ticket_management.unit.shared.handlerException;

import io.ennov.ticket_management.shared.handlerException.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidationErrorTest {

    @Test
    @DisplayName("Should create ValidationError with all args constructor")
    void shouldCreateValidationErrorWithAllArgsConstructor() {
        // Given
        String property = "username";
        String message = "Username is required";

        // When
        ValidationError error = new ValidationError(property, message);

        // Then
        assertThat(error.getProperty()).isEqualTo(property);
        assertThat(error.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Should allow setting and getting properties")
    void shouldAllowSettingAndGettingProperties() {
        // Given
        ValidationError error = new ValidationError("email", "Invalid email format");

        // When
        error.setProperty("password");
        error.setMessage("Password is too short");

        // Then
        assertThat(error.getProperty()).isEqualTo("password");
        assertThat(error.getMessage()).isEqualTo("Password is too short");
    }

    @Test
    @DisplayName("Should be equal for same values")
    void shouldBeEqualForSameValues() {
        // Given
        ValidationError error1 = new ValidationError("age", "Must be positive");
        ValidationError error2 = new ValidationError("age", "Must be positive");

        // When/Then
        assertThat(error1).isEqualTo(error2);
        assertThat(error1.hashCode()).isEqualTo(error2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal for different values")
    void shouldNotBeEqualForDifferentValues() {
        // Given
        ValidationError error1 = new ValidationError("name", "Cannot be empty");
        ValidationError error2 = new ValidationError("name", "Must be at least 3 characters");

        // When/Then
        assertThat(error1).isNotEqualTo(error2);
        assertThat(error1.hashCode()).isNotEqualTo(error2.hashCode());
    }

    @Test
    @DisplayName("Should have correct toString representation")
    void shouldHaveCorrectToStringRepresentation() {
        // Given
        ValidationError error = new ValidationError("phone", "Invalid phone number");

        // When
        String toStringResult = error.toString();

        // Then
        assertThat(toStringResult)
                .contains("ValidationError")
                .contains("property=phone")
                .contains("message=Invalid phone number");
    }
}
