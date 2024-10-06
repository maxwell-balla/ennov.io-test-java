package io.ennov.ticket_management.unities.domain;

import io.ennov.ticket_management.domain.Ticket;
import io.ennov.ticket_management.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Nested
    @DisplayName("User construction tests")
    class UserConstructionTests {

        @Test
        @DisplayName("Should create user with all args constructor")
        void shouldCreateUserWithAllArgsConstructor() {
            // Given
            Long id = 1L;
            String username = "user";
            String email = "test@gmail.com";
            List<Ticket> tickets = new ArrayList<>();

            // When
            User user = new User(id, username, email, tickets);

            // Then
            assertThat(user).isNotNull()
                    .hasFieldOrPropertyWithValue("id", id)
                    .hasFieldOrPropertyWithValue("username", username)
                    .hasFieldOrPropertyWithValue("email", email)
                    .hasFieldOrPropertyWithValue("ticketList", tickets);
        }

        @Test
        @DisplayName("Should create user with builder")
        void shouldCreateUserWithBuilder() {
            // Given
            Long id = 2L;
            String username = "builderUser";
            String email = "builder@gmail.com";
            List<Ticket> tickets = new ArrayList<>();

            // When
            User user = User.builder()
                    .id(id)
                    .username(username)
                    .email(email)
                    .ticketList(tickets)
                    .build();

            // Then
            assertThat(user).isNotNull()
                    .hasFieldOrPropertyWithValue("id", id)
                    .hasFieldOrPropertyWithValue("username", username)
                    .hasFieldOrPropertyWithValue("email", email)
                    .hasFieldOrPropertyWithValue("ticketList", tickets);
        }
    }

    @Nested
    @DisplayName("User property tests")
    class UserPropertyTests {

        private User user;

        @BeforeEach
        void setUp() {
            user = new User();
        }

        @Test
        @DisplayName("Should set and get id")
        void shouldSetAndGetId() {
            // Given
            Long id = 3L;

            // When
            user.setId(id);

            // Then
            assertThat(user.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should set and get username")
        void shouldSetAndGetUsername() {
            // Given
            String username = "newUser";

            // When
            user.setUsername(username);

            // Then
            assertThat(user.getUsername()).isEqualTo(username);
        }

        @Test
        @DisplayName("Should set and get email")
        void shouldSetAndGetEmail() {
            // Given
            String email = "new@example.com";

            // When
            user.setEmail(email);

            // Then
            assertThat(user.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("Should set and get ticket list")
        void shouldSetAndGetTicketList() {
            // Given
            List<Ticket> tickets = new ArrayList<>();
            tickets.add(new Ticket());

            // When
            user.setTicketList(tickets);

            // Then
            assertThat(user.getTicketList())
                    .isNotNull()
                    .isEqualTo(tickets)
                    .hasSize(1);
        }
    }
}
