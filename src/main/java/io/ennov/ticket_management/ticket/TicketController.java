package io.ennov.ticket_management.ticket;

import io.ennov.ticket_management.shared.AssignedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(
            @RequestBody TicketDto ticketDto
    ) {
        TicketDto newTicket = ticketService.createTicket(ticketDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTicket);
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        List<TicketDto> ticketList = ticketService.findAllTickets();
        return ResponseEntity.status(HttpStatus.OK).body(ticketList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(
            @PathVariable Long id
    ) {
        TicketDto ticketDto = ticketService.findTicketById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ticketDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(
            @PathVariable Long id,
            @RequestBody TicketDto ticketDto
    ) {
        TicketDto newTicket = ticketService.modifyTicket(id, ticketDto);
        return ResponseEntity.status(HttpStatus.OK).body(newTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long id
    ) {
        ticketService.deleteTicket(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/assign/{userId}")
    public ResponseEntity<AssignedDto> assignTicket(
            @PathVariable Long id,
            @PathVariable Long userId
    ) {
        AssignedDto assignedDto = ticketService.assignTicket(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(assignedDto);
    }
}
