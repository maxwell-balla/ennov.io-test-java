package io.ennov.ticket_management.ticket;

import io.ennov.ticket_management.shared.AssignedDto;
import io.ennov.ticket_management.shared.ConflictAssignException;
import io.ennov.ticket_management.user.User;
import io.ennov.ticket_management.user.UserNotFoundException;
import io.ennov.ticket_management.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found: ";
    private static final String TICKET_NOT_FOUND_MESSAGE = "Ticket not found: ";

    @Transactional
    public TicketDto createTicket(TicketDto ticketDto) {
        Ticket ticket = ticketMapper.ticketDtoToTicket(ticketDto);
        return ticketMapper.ticketToTicketDto(ticketRepository.save(ticket));
    }

    public List<TicketDto> findAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public TicketDto findTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND_MESSAGE + id));

        return ticketMapper.ticketToTicketDto(ticket);
    }

    @Transactional
    public TicketDto modifyTicket(Long id, TicketDto ticketDto) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND_MESSAGE + id));
        Ticket newTicket = mergeToNewTicket(existingTicket, ticketDto);
        return ticketMapper.ticketToTicketDto(ticketRepository.save(newTicket));
    }

    private Ticket mergeToNewTicket(Ticket existingTicket, TicketDto ticketDto) {
        if (ticketDto.title() != null && !ticketDto.title().equals(existingTicket.getTitle())) {
            existingTicket.setTitle(ticketDto.title());
        }
        if (ticketDto.description() != null && !ticketDto.description().equals(existingTicket.getDescription())) {
            existingTicket.setDescription(ticketDto.description());
        }
        if (ticketDto.status() != null && !ticketDto.status().equals(existingTicket.getStatus())) {
            existingTicket.setStatus(ticketDto.status());
        }
        return existingTicket;
    }

    public void deleteTicket(Long id) {
        ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND_MESSAGE + id));

        ticketRepository.deleteById(id);
    }

    @Transactional
    public AssignedDto assignTicket(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE + userId));
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(TICKET_NOT_FOUND_MESSAGE + id));
        if (ticket.getUser() != null) {
            throw new ConflictAssignException("Ticket is already assigned to another user");
        }
        ticket.setUser(user);
        ticketRepository.save(ticket);
        return mergeToAssignedDto(user, ticket);
    }

    private AssignedDto mergeToAssignedDto(User user, Ticket ticket) {
        return new AssignedDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                ticketMapper.ticketToTicketDto(ticket)
        );
    }
}
