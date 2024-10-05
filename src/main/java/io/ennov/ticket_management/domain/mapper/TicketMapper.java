package io.ennov.ticket_management.domain.mapper;

import io.ennov.ticket_management.domain.Ticket;
import io.ennov.ticket_management.dto.TicketDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketDto ticketToTicketDto(Ticket ticket);
}
