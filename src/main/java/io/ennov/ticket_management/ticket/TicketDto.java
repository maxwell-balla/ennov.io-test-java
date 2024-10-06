package io.ennov.ticket_management.ticket;

public record TicketDto(
        Long id,
        String title,
        String description,
        StatusTicket status
) {
}