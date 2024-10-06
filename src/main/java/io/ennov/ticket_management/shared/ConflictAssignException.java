package io.ennov.ticket_management.shared;

public class ConflictAssignException extends RuntimeException {
    public ConflictAssignException(String message) {
        super(message);
    }
}
