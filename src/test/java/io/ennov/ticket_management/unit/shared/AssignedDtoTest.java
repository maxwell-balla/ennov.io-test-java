package io.ennov.ticket_management.unit.shared;

import io.ennov.ticket_management.shared.AssignedDto;
import io.ennov.ticket_management.ticket.StatusTicket;
import io.ennov.ticket_management.ticket.TicketDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AssignedDtoTest {

    @Test
    @DisplayName("Should create AssignedDto with all fields")
    void shouldCreateAssignedDtoWithAllFields() {
        // Given
        Long userId = 1L;
        String username = "testuser";
        String email = "test@example.com";
        TicketDto ticketDto = new TicketDto(1L, "Test Ticket", "Description", StatusTicket.PENDING);

        // When
        AssignedDto assignedDto = new AssignedDto(userId, username, email, ticketDto);

        // Then
        assertThat(assignedDto.userId()).isEqualTo(userId);
        assertThat(assignedDto.username()).isEqualTo(username);
        assertThat(assignedDto.email()).isEqualTo(email);
        assertThat(assignedDto.tickets()).isEqualTo(ticketDto);
    }

    @Test
    @DisplayName("Should be equal for same values")
    void shouldBeEqualForSameValues() {
        // Given
        TicketDto ticketDto = new TicketDto(1L, "Test Ticket", "Description", StatusTicket.PENDING);
        AssignedDto assignedDto1 = new AssignedDto(1L, "testuser", "test@example.com", ticketDto);
        AssignedDto assignedDto2 = new AssignedDto(1L, "testuser", "test@example.com", ticketDto);

        // When/Then
        assertThat(assignedDto1).isEqualTo(assignedDto2);
        assertThat(assignedDto1.hashCode()).isEqualTo(assignedDto2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal for different values")
    void shouldNotBeEqualForDifferentValues() {
        // Given
        TicketDto ticketDto1 = new TicketDto(1L, "Test Ticket 1", "Description 1", StatusTicket.PENDING);
        TicketDto ticketDto2 = new TicketDto(2L, "Test Ticket 2", "Description 2", StatusTicket.DONE);
        AssignedDto assignedDto1 = new AssignedDto(1L, "user1", "user1@example.com", ticketDto1);
        AssignedDto assignedDto2 = new AssignedDto(2L, "user2", "user2@example.com", ticketDto2);

        // When/Then
        assertThat(assignedDto1).isNotEqualTo(assignedDto2);
        assertThat(assignedDto1.hashCode()).isNotEqualTo(assignedDto2.hashCode());
    }

    @Test
    @DisplayName("Should have correct toString representation")
    void shouldHaveCorrectToStringRepresentation() {
        // Given
        Long userId = 1L;
        String username = "testuser";
        String email = "test@example.com";
        TicketDto ticketDto = new TicketDto(1L, "Test Ticket", "Description", StatusTicket.PENDING);
        AssignedDto assignedDto = new AssignedDto(userId, username, email, ticketDto);

        // When
        String toStringResult = assignedDto.toString();

        // Then
        assertThat(toStringResult)
                .contains("AssignedDto")
                .contains("userId=" + userId)
                .contains("username=" + username)
                .contains("email=" + email)
                .contains("tickets=" + ticketDto.toString());
    }

    @Test
    @DisplayName("Should allow null values")
    void shouldAllowNullValues() {
        // Given/When
        AssignedDto assignedDto = new AssignedDto(null, null, null, null);

        // Then
        assertThat(assignedDto.userId()).isNull();
        assertThat(assignedDto.username()).isNull();
        assertThat(assignedDto.email()).isNull();
        assertThat(assignedDto.tickets()).isNull();
    }
}
