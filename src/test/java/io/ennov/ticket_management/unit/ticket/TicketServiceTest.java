package io.ennov.ticket_management.unit.ticket;

import io.ennov.ticket_management.shared.AssignedDto;
import io.ennov.ticket_management.shared.ConflictAssignException;
import io.ennov.ticket_management.ticket.StatusTicket;
import io.ennov.ticket_management.ticket.Ticket;
import io.ennov.ticket_management.ticket.TicketMapper;
import io.ennov.ticket_management.ticket.TicketDto;
import io.ennov.ticket_management.ticket.TicketNotFoundException;
import io.ennov.ticket_management.ticket.TicketRepository;
import io.ennov.ticket_management.ticket.TicketService;
import io.ennov.ticket_management.user.User;
import io.ennov.ticket_management.user.UserNotFoundException;
import io.ennov.ticket_management.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private TicketService ticketService;

    @Nested
    @DisplayName("Create Ticket Tests")
    class CreateTicketTests {

        @Test
        @DisplayName("Should create ticket successfully")
        void shouldCreateTicketSuccessfully() {
            // Given
            TicketDto inputDto = new TicketDto(null, "New Ticket", "Description", null);
            Ticket ticket = new Ticket();
            Ticket savedTicket = new Ticket();
            TicketDto outputDto = new TicketDto(1L, "New Ticket", "Description", null);

            when(ticketMapper.ticketDtoToTicket(inputDto)).thenReturn(ticket);
            when(ticketRepository.save(ticket)).thenReturn(savedTicket);
            when(ticketMapper.ticketToTicketDto(savedTicket)).thenReturn(outputDto);

            // When
            TicketDto result = ticketService.createTicket(inputDto);

            // Then
            assertThat(result).isEqualTo(outputDto);
        }
    }

    @Nested
    @DisplayName("Find All Tickets Tests")
    class FindAllTicketsTests {

        @Test
        @DisplayName("Should return all tickets")
        void shouldReturnAllTickets() {
            // Given
            List<Ticket> tickets = Arrays.asList(new Ticket(), new Ticket());
            List<TicketDto> ticketDto = Arrays.asList(
                    new TicketDto(1L, "Ticket 1", "Description 1", null),
                    new TicketDto(2L, "Ticket 2", "Description 2", null)
            );

            when(ticketRepository.findAll()).thenReturn(tickets);
            when(ticketMapper.ticketToTicketDto(tickets.get(0))).thenReturn(ticketDto.get(0));
            when(ticketMapper.ticketToTicketDto(tickets.get(1))).thenReturn(ticketDto.get(1));

            // When
            List<TicketDto> result = ticketService.findAllTickets();

            // Then
            assertThat(result).hasSize(2).containsExactlyElementsOf(ticketDto);
        }

        @Test
        @DisplayName("Should return empty list when no tickets exist")
        void shouldReturnEmptyListWhenNoTicketsExist() {
            // Given
            when(ticketRepository.findAll()).thenReturn(List.of());

            // When
            List<TicketDto> result = ticketService.findAllTickets();

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Find Ticket By Id Tests")
    class FindTicketByIdTests {

        @Test
        @DisplayName("Should return ticket when it exists")
        void shouldReturnTicketWhenItExists() {
            // Given
            Long ticketId = 1L;
            Ticket ticket = new Ticket();
            TicketDto ticketDto = new TicketDto(ticketId, "Test Ticket", "Description", StatusTicket.PENDING);

            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
            when(ticketMapper.ticketToTicketDto(ticket)).thenReturn(ticketDto);

            // When
            TicketDto result = ticketService.findTicketById(ticketId);

            // Then
            assertThat(result).isEqualTo(ticketDto);
        }

        @Test
        @DisplayName("Should throw TicketNotFoundException when ticket doesn't exist")
        void shouldThrowTicketNotFoundExceptionWhenTicketDoesNotExist() {
            // Given
            Long ticketId = 999L;
            when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> ticketService.findTicketById(ticketId))
                    .isInstanceOf(TicketNotFoundException.class)
                    .hasMessageContaining("Ticket not found: " + ticketId);
        }
    }

    @Nested
    @DisplayName("Modify Ticket Tests")
    class ModifyTicketTests {

        @Test
        @DisplayName("Should update ticket with all new fields")
        void shouldUpdateTicketWithAllNewFields() {
            // Given
            Long ticketId = 1L;
            Ticket existingTicket = new Ticket(ticketId, "Old Title", "Old Description", StatusTicket.PENDING, null);
            TicketDto inputDto = new TicketDto(null, "New Title", "New Description", StatusTicket.DONE);
            Ticket updatedTicket = new Ticket(ticketId, "New Title", "New Description", StatusTicket.DONE, null);
            TicketDto outputDto = new TicketDto(ticketId, "New Title", "New Description", StatusTicket.DONE);

            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
            when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);
            when(ticketMapper.ticketToTicketDto(updatedTicket)).thenReturn(outputDto);

            // When
            TicketDto result = ticketService.modifyTicket(ticketId, inputDto);

            // Then
            assertThat(result).isEqualTo(outputDto);
            assertThat(result.title()).isEqualTo("New Title");
            assertThat(result.description()).isEqualTo("New Description");
            assertThat(result.status()).isEqualTo(StatusTicket.DONE);
        }

        @Test
        @DisplayName("Should update ticket with partial fields")
        void shouldUpdateTicketWithPartialFields() {
            // Given
            Long ticketId = 1L;
            Ticket existingTicket = new Ticket(ticketId, "Old Title", "Old Description", StatusTicket.PENDING, null);
            TicketDto inputDto = new TicketDto(null, "New Title", null, null);
            Ticket updatedTicket = new Ticket(ticketId, "New Title", "Old Description", StatusTicket.PENDING, null);
            TicketDto outputDto = new TicketDto(ticketId, "New Title", "Old Description", StatusTicket.PENDING);

            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
            when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);
            when(ticketMapper.ticketToTicketDto(updatedTicket)).thenReturn(outputDto);

            // When
            TicketDto result = ticketService.modifyTicket(ticketId, inputDto);

            // Then
            assertThat(result).isEqualTo(outputDto);
            assertThat(result.title()).isEqualTo("New Title");
            assertThat(result.description()).isEqualTo("Old Description");
            assertThat(result.status()).isEqualTo(StatusTicket.PENDING);
        }

        @Test
        @DisplayName("Should not update ticket when no changes are provided")
        void shouldNotUpdateTicketWhenNoChangesAreProvided() {
            // Given
            Long ticketId = 1L;
            Ticket existingTicket = new Ticket(ticketId, "Title", "Description", StatusTicket.PENDING, null);
            TicketDto inputDto = new TicketDto(null, "Title", "Description", StatusTicket.PENDING);
            TicketDto outputDto = new TicketDto(ticketId, "Title", "Description", StatusTicket.PENDING);

            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(existingTicket));
            when(ticketRepository.save(any(Ticket.class))).thenReturn(existingTicket);
            when(ticketMapper.ticketToTicketDto(existingTicket)).thenReturn(outputDto);

            // When
            TicketDto result = ticketService.modifyTicket(ticketId, inputDto);

            // Then
            assertThat(result).isEqualTo(outputDto);
        }

        @Test
        @DisplayName("Should throw TicketNotFoundException when ticket doesn't exist")
        void shouldThrowTicketNotFoundExceptionWhenTicketDoesNotExist() {
            // Given
            Long ticketId = 999L;
            TicketDto inputDto = new TicketDto(null, "New Title", "New Description", StatusTicket.DONE);

            when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> ticketService.modifyTicket(ticketId, inputDto))
                    .isInstanceOf(TicketNotFoundException.class)
                    .hasMessageContaining("Ticket not found: " + ticketId);
        }
    }

    @Nested
    @DisplayName("Delete Ticket Tests")
    class DeleteTicketTests {

        @Test
        @DisplayName("Should delete ticket successfully")
        void shouldDeleteTicketSuccessfully() {
            // Given
            Long ticketId = 1L;
            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(new Ticket()));
            doNothing().when(ticketRepository).deleteById(ticketId);

            // When
            ticketService.deleteTicket(ticketId);

            // Then
            verify(ticketRepository).findById(ticketId);
            verify(ticketRepository).deleteById(ticketId);
        }

        @Test
        @DisplayName("Should throw TicketNotFoundException when ticket doesn't exist")
        void shouldThrowTicketNotFoundExceptionWhenTicketDoesNotExist() {
            // Given
            Long ticketId = 999L;
            when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> ticketService.deleteTicket(ticketId))
                    .isInstanceOf(TicketNotFoundException.class)
                    .hasMessageContaining("Ticket not found: " + ticketId);

            verify(ticketRepository).findById(ticketId);
            verify(ticketRepository, never()).deleteById(anyLong());
        }
    }

    @Nested
    @DisplayName("Assign Ticket Tests")
    class AssignTicketTests {

        @Test
        @DisplayName("Should assign ticket successfully")
        void shouldAssignTicketSuccessfully() {
            // Given
            Long ticketId = 1L;
            Long userId = 2L;
            User user = new User(userId, "testuser", "test@example.com", null);
            Ticket ticket = new Ticket(ticketId, "Test Ticket", "Description", StatusTicket.PENDING, null);
            TicketDto ticketDto = new TicketDto(ticketId, "Test Ticket", "Description", StatusTicket.PENDING);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
            when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
            when(ticketMapper.ticketToTicketDto(ticket)).thenReturn(ticketDto);

            // When
            AssignedDto result = ticketService.assignTicket(ticketId, userId);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.userId()).isEqualTo(userId);
            assertThat(result.username()).isEqualTo("testuser");
            assertThat(result.email()).isEqualTo("test@example.com");
            assertThat(result.tickets()).isEqualTo(ticketDto);

            verify(ticketRepository).save(ticket);
            assertThat(ticket.getUser()).isEqualTo(user);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user doesn't exist")
        void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
            // Given
            Long ticketId = 1L;
            Long userId = 999L;

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> ticketService.assignTicket(ticketId, userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User not found: " + userId);

            verify(ticketRepository, never()).findById(anyLong());
            verify(ticketRepository, never()).save(any(Ticket.class));
        }

        @Test
        @DisplayName("Should throw TicketNotFoundException when ticket doesn't exist")
        void shouldThrowTicketNotFoundExceptionWhenTicketDoesNotExist() {
            // Given
            Long ticketId = 999L;
            Long userId = 1L;
            User user = new User(userId, "testuser", "test@example.com", null);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> ticketService.assignTicket(ticketId, userId))
                    .isInstanceOf(TicketNotFoundException.class)
                    .hasMessageContaining("Ticket not found: " + ticketId);

            verify(ticketRepository, never()).save(any(Ticket.class));
        }

        @Test
        @DisplayName("Should throw ConflictAssignException when ticket is already assigned")
        void shouldThrowConflictAssignExceptionWhenTicketIsAlreadyAssigned() {
            // Given
            Long ticketId = 1L;
            Long userId = 2L;
            User existingUser = new User(3L, "existinguser", "existing@example.com", null);
            Ticket ticket = new Ticket(ticketId, "Test Ticket", "Description", StatusTicket.PENDING, existingUser);

            when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
            when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

            // When/Then
            assertThatThrownBy(() -> ticketService.assignTicket(ticketId, userId))
                    .isInstanceOf(ConflictAssignException.class)
                    .hasMessage("Ticket is already assigned to another user");

            verify(ticketRepository, never()).save(any(Ticket.class));
        }
    }
}
