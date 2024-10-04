package io.ennov.ticket_management.unities.domains;

import io.ennov.ticket_management.domain.StatusTicket;
import io.ennov.ticket_management.domain.Ticket;
import io.ennov.ticket_management.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TicketTest {

    @Nested
    @DisplayName("Ticket construction tests")
    class TicketConstructionTests {

        @Test
        @DisplayName("Should create ticket with all args constructor")
        void shouldCreateTicketWithAllArgsConstructor() {
            // Given
            Long id = 1L;
            String title = "Test Ticket";
            String description = "This is a test ticket";
            StatusTicket status = StatusTicket.DONE;
            User user = new User();

            // When
            Ticket ticket = new Ticket(id, title, description, status, user);

            // Then
            assertThat(ticket).isNotNull()
                    .hasFieldOrPropertyWithValue("id", id)
                    .hasFieldOrPropertyWithValue("title", title)
                    .hasFieldOrPropertyWithValue("description", description)
                    .hasFieldOrPropertyWithValue("statusTicket", status)
                    .hasFieldOrPropertyWithValue("user", user);
        }

        @Test
        @DisplayName("Should create ticket with builder")
        void shouldCreateTicketWithBuilder() {
            // Given
            Long id = 2L;
            String title = "Builder Ticket";
            String description = "This ticket is created with a builder";
            StatusTicket status = StatusTicket.DONE;
            User user = new User();

            // When
            Ticket ticket = Ticket.builder()
                    .id(id)
                    .title(title)
                    .description(description)
                    .statusTicket(status)
                    .user(user)
                    .build();

            // Then
            assertThat(ticket).isNotNull()
                    .hasFieldOrPropertyWithValue("id", id)
                    .hasFieldOrPropertyWithValue("title", title)
                    .hasFieldOrPropertyWithValue("description", description)
                    .hasFieldOrPropertyWithValue("statusTicket", status)
                    .hasFieldOrPropertyWithValue("user", user);
        }
    }

    @Nested
    @DisplayName("Ticket property tests")
    class TicketPropertyTests {

        private Ticket ticket;

        @BeforeEach
        void setUp() {
            ticket = new Ticket();
        }

        @Test
        @DisplayName("Should set and get id")
        void shouldSetAndGetId() {
            // Given
            Long id = 3L;

            // When
            ticket.setId(id);

            // Then
            assertThat(ticket.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should set and get title")
        void shouldSetAndGetTitle() {
            // Given
            String title = "New Ticket Title";

            // When
            ticket.setTitle(title);

            // Then
            assertThat(ticket.getTitle()).isEqualTo(title);
        }

        @Test
        @DisplayName("Should set and get description")
        void shouldSetAndGetDescription() {
            // Given
            String description = "This is a new description for the ticket";

            // When
            ticket.setDescription(description);

            // Then
            assertThat(ticket.getDescription()).isEqualTo(description);
        }

        @Test
        @DisplayName("Should set and get status")
        void shouldSetAndGetStatus() {
            // Given
            StatusTicket status = StatusTicket.DONE;

            // When
            ticket.setStatusTicket(status);

            // Then
            assertThat(ticket.getStatusTicket()).isEqualTo(status);
        }

        @Test
        @DisplayName("Should set and get user")
        void shouldSetAndGetUser() {
            // Given
            User user = new User();
            user.setId(1L);
            user.setUsername("testUser");

            // When
            ticket.setUser(user);

            // Then
            assertThat(ticket.getUser())
                    .isNotNull()
                    .isEqualTo(user)
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("username", "testUser");
        }
    }
}
