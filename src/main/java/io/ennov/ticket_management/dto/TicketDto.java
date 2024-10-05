package io.ennov.ticket_management.dto;

import io.ennov.ticket_management.domain.StatusTicket;

public record TicketDto(
        Long id,
        String title,
        String description,
        StatusTicket status,
        Long userId
) {
}