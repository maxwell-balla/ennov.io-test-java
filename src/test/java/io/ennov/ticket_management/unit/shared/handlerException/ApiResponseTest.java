package io.ennov.ticket_management.unit.shared.handlerException;

import io.ennov.ticket_management.shared.handlerException.ApiResponse;
import io.ennov.ticket_management.shared.handlerException.ValidationError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiResponseTest {

    @Test
    @DisplayName("Should create ApiResponse with basic constructor")
    void shouldCreateApiResponseWithBasicConstructor() {
        // Given
        Integer status = 200;
        Boolean success = true;
        Date timestamp = new Date();
        String message = "Success";
        String details = "Operation completed successfully";

        // When
        ApiResponse response = new ApiResponse(status, success, timestamp, message, details);

        // Then
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getSuccess()).isEqualTo(success);
        assertThat(response.getTimestamp()).isEqualTo(timestamp);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getDetails()).isEqualTo(details);
        assertThat(response.getData()).isNull();
        assertThat(response.getErrors()).isNull();
    }

    @Test
    @DisplayName("Should create ApiResponse with errors constructor")
    void shouldCreateApiResponseWithErrorsConstructor() {
        // Given
        Integer status = 400;
        Boolean success = false;
        Date timestamp = new Date();
        String message = "Validation Error";
        String details = "Invalid input";
        List<ValidationError> errors = Arrays.asList(
                new ValidationError("field1", "Error 1"),
                new ValidationError("field2", "Error 2")
        );

        // When
        ApiResponse response = new ApiResponse(status, success, timestamp, message, details, errors);

        // Then
        assertThat(response.getStatus()).isEqualTo(status);
        assertThat(response.getSuccess()).isEqualTo(success);
        assertThat(response.getTimestamp()).isEqualTo(timestamp);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getDetails()).isEqualTo(details);
        assertThat(response.getErrors()).isEqualTo(errors);
        assertThat(response.getData()).isNull();
    }

    @Test
    @DisplayName("Should set and get data")
    void shouldSetAndGetData() {
        // Given
        ApiResponse response = new ApiResponse(200, true, new Date(), "Success", "Details");
        Object data = new Object();

        // When
        response.setData(data);

        // Then
        assertThat(response.getData()).isEqualTo(data);
    }

    @Test
    @DisplayName("Should use Lombok generated methods")
    void shouldUseLombokGeneratedMethods() {
        // Given
        ApiResponse response = new ApiResponse(200, true, new Date(), "Success", "Details");

        // When
        response.setStatus(201);
        response.setSuccess(false);
        Date newTimestamp = new Date();
        response.setTimestamp(newTimestamp);
        response.setMessage("Updated");
        response.setDetails("New details");

        // Then
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getSuccess()).isFalse();
        assertThat(response.getTimestamp()).isEqualTo(newTimestamp);
        assertThat(response.getMessage()).isEqualTo("Updated");
        assertThat(response.getDetails()).isEqualTo("New details");
    }

    @Test
    @DisplayName("Should generate correct toString representation")
    void shouldGenerateCorrectToStringRepresentation() {
        // Given
        ApiResponse response = new ApiResponse(200, true, new Date(), "Success", "Details");

        // When
        String toStringResult = response.toString();

        // Then
        assertThat(toStringResult)
                .contains("ApiResponse")
                .contains("status=200")
                .contains("success=true")
                .contains("message=Success")
                .contains("details=Details");
    }
}
