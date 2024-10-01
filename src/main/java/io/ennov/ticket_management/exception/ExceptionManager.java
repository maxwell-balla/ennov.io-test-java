package io.ennov.ticket_management.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Throwables;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
@Slf4j
public class ExceptionManager extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred: ", ex);
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, "An unexpected error occurred");
    }

    private ResponseEntity<Object> buildResponseEntity(Exception ex, HttpStatus status, WebRequest request, String defaultMessage) {
        String errorMessage = ex.getMessage();
        Throwable rootCause = Throwables.getRootCause(ex);
        if (rootCause != null && !errorMessage.equals(rootCause.getMessage())) {
            errorMessage = rootCause.getMessage();
        }

        if (errorMessage == null || errorMessage.isBlank()) {
            errorMessage = defaultMessage;
        }

        ApiResponse apiResponse = new ApiResponse(
                status.value(),
                false,
                new Date(),
                errorMessage,
                request.getDescription(false)
        );

        return new ResponseEntity<>(apiResponse, status);
    }
}
