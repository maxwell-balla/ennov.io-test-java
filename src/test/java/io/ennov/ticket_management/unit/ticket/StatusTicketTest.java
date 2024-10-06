package io.ennov.ticket_management.unit.ticket;

import io.ennov.ticket_management.ticket.StatusTicket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StatusTicketTest {

    @Test
    @DisplayName("Should have the correct number of enum values")
    void shouldHaveCorrectNumberOfEnumValues() {
        // Given
        int expectedNumberOfValues = 3;

        // When
        StatusTicket[] statusValues = StatusTicket.values();

        // Then
        assertThat(statusValues).hasSize(expectedNumberOfValues);
    }

    @Test
    @DisplayName("Should contain all expected enum values")
    void shouldContainAllExpectedEnumValues() {
        // Given
        StatusTicket[] expectedValues = {StatusTicket.PENDING, StatusTicket.DONE, StatusTicket.CANCEL};

        // When
        StatusTicket[] actualValues = StatusTicket.values();

        // Then
        assertThat(actualValues).containsExactlyInAnyOrder(expectedValues);
    }

    @Test
    @DisplayName("Should correctly convert enum to string")
    void shouldCorrectlyConvertEnumToString() {
        // Given/When/Then
        assertThat(StatusTicket.PENDING.toString()).isEqualTo("PENDING");
        assertThat(StatusTicket.DONE.toString()).isEqualTo("DONE");
        assertThat(StatusTicket.CANCEL.toString()).isEqualTo("CANCEL");
    }

    @Test
    @DisplayName("Should correctly convert string to enum")
    void shouldCorrectlyConvertStringToEnum() {
        // Given/When/Then
        assertThat(StatusTicket.valueOf("PENDING")).isEqualTo(StatusTicket.PENDING);
        assertThat(StatusTicket.valueOf("DONE")).isEqualTo(StatusTicket.DONE);
        assertThat(StatusTicket.valueOf("CANCEL")).isEqualTo(StatusTicket.CANCEL);
    }
}
