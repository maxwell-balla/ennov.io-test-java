package io.ennov.ticket_management.ticket;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    TicketDto ticketToTicketDto(Ticket ticket);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", defaultValue = "PENDING")
    Ticket ticketDtoToTicket(TicketDto ticketDto);
}