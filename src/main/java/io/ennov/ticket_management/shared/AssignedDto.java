package io.ennov.ticket_management.shared;

import io.ennov.ticket_management.ticket.TicketDto;

public record AssignedDto(
        Long userId,
        String username,
        String email,
        TicketDto tickets
) {
}
