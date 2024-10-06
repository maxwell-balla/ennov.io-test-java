package io.ennov.ticket_management.unit.ticket;

import io.ennov.ticket_management.ticket.StatusTicket;
import io.ennov.ticket_management.ticket.TicketDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TicketDtoTest {

    @Test
    @DisplayName("Should create TicketDto with all fields")
    void shouldCreateTicketDtoWithAllFields() {
        // Given
        Long id = 1L;
        String title = "Test Ticket";
        String description = "This is a test ticket";
        StatusTicket status = StatusTicket.PENDING;

        // When
        TicketDto ticketDto = new TicketDto(id, title, description, status);

        // Then
        assertThat(ticketDto.id()).isEqualTo(id);
        assertThat(ticketDto.title()).isEqualTo(title);
        assertThat(ticketDto.description()).isEqualTo(description);
        assertThat(ticketDto.status()).isEqualTo(status);
    }

    @Test
    @DisplayName("Should be equal for same values")
    void shouldBeEqualForSameValues() {
        // Given
        TicketDto ticketDto1 = new TicketDto(1L, "Test", "Description", StatusTicket.PENDING);
        TicketDto ticketDto2 = new TicketDto(1L, "Test", "Description", StatusTicket.PENDING);

        // When/Then
        assertThat(ticketDto1).isEqualTo(ticketDto2);
        assertThat(ticketDto1.hashCode()).isEqualTo(ticketDto2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal for different values")
    void shouldNotBeEqualForDifferentValues() {
        // Given
        TicketDto ticketDto1 = new TicketDto(1L, "Test1", "Description1", StatusTicket.PENDING);
        TicketDto ticketDto2 = new TicketDto(2L, "Test2", "Description2", StatusTicket.DONE);

        // When/Then
        assertThat(ticketDto1).isNotEqualTo(ticketDto2);
        assertThat(ticketDto1.hashCode()).isNotEqualTo(ticketDto2.hashCode());
    }

    @Test
    @DisplayName("Should have correct toString representation")
    void shouldHaveCorrectToStringRepresentation() {
        // Given
        TicketDto ticketDto = new TicketDto(1L, "Test", "Description", StatusTicket.PENDING);

        // When
        String toStringResult = ticketDto.toString();

        // Then
        assertThat(toStringResult)
                .contains("TicketDto")
                .contains("title=Test")
                .contains("description=Description")
                .contains("status=PENDING");
    }
}
