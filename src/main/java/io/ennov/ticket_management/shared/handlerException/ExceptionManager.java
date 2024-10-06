package io.ennov.ticket_management.shared.handlerException;

import io.ennov.ticket_management.user.EmailAlreadyExistsException;
import io.ennov.ticket_management.ticket.TicketNotFoundException;
import io.ennov.ticket_management.user.UserNotFoundException;
import io.ennov.ticket_management.user.UsernameAlreadyExistsException;
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

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<Object> handleTicketNotFoundException(TicketNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExists(EmailAlreadyExistsException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        return buildResponseEntity(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, "An unexpected error occurred");
    }

    private ResponseEntity<Object> buildResponseEntity(Exception ex, HttpStatus status, WebRequest request, String defaultMessage) {
        log.error("Exception occurred: ", ex);

        String errorMessage = ex.getMessage();
        Throwable rootCause = Throwables.getRootCause(ex);
        if (rootCause != null && !errorMessage.equals(rootCause.getMessage())) {
            errorMessage = rootCause.getMessage();
        }

        if (errorMessage == null || errorMessage.isBlank()) {
            errorMessage = defaultMessage;
        }

        ApiResponse apiResponse = new ApiResponse(status.value(), false, new Date(), errorMessage, request.getDescription(false));
        return new ResponseEntity<>(apiResponse, status);
    }
}
