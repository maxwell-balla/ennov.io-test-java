package io.ennov.ticket_management.ticket;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String message) {
        super(message);
    }
}
