package io.ennov.ticket_management.unit.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ennov.ticket_management.shared.AssignedDto;
import io.ennov.ticket_management.ticket.TicketController;
import io.ennov.ticket_management.ticket.StatusTicket;
import io.ennov.ticket_management.ticket.TicketDto;
import io.ennov.ticket_management.ticket.TicketNotFoundException;
import io.ennov.ticket_management.ticket.TicketService;
import io.ennov.ticket_management.user.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("Create Ticket Tests")
    class CreateTicketTests {

        @Test
        @DisplayName("Should create a new ticket")
        void shouldCreateNewTicket() throws Exception {
            // Given
            TicketDto inputDto = new TicketDto(null, "New Ticket", "Description", StatusTicket.PENDING);
            TicketDto outputDto = new TicketDto(1L, "New Ticket", "Description", StatusTicket.PENDING);

            when(ticketService.createTicket(any(TicketDto.class))).thenReturn(outputDto);

            // When & Then
            mockMvc.perform(post("/tickets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("New Ticket"))
                    .andExpect(jsonPath("$.description").value("Description"))
                    .andExpect(jsonPath("$.status").value("PENDING"));
        }

        @Test
        @DisplayName("Should handle TicketService exception")
        void shouldHandleTicketServiceException() throws Exception {
            // Given
            TicketDto inputDto = new TicketDto(null, "New Ticket", "Description", null);

            when(ticketService.createTicket(any(TicketDto.class))).thenThrow(new RuntimeException("Service Error"));

            // When & Then
            mockMvc.perform(post("/tickets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Get All Tickets Tests")
    class GetAllTicketsTests {

        @Test
        @DisplayName("Should return all tickets")
        void shouldReturnAllTickets() throws Exception {
            // Given
            List<TicketDto> tickets = Arrays.asList(
                    new TicketDto(1L, "Ticket 1", "Description 1", StatusTicket.PENDING),
                    new TicketDto(2L, "Ticket 2", "Description 2", StatusTicket.DONE)
            );
            when(ticketService.findAllTickets()).thenReturn(tickets);

            // When & Then
            mockMvc.perform(get("/tickets")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].title").value("Ticket 1"))
                    .andExpect(jsonPath("$[1].id").value(2))
                    .andExpect(jsonPath("$[1].title").value("Ticket 2"));
        }

        @Test
        @DisplayName("Should return empty list when no tickets exist")
        void shouldReturnEmptyListWhenNoTicketsExist() throws Exception {
            // Given
            when(ticketService.findAllTickets()).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/tickets")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Should handle exception when service throws error")
        void shouldHandleExceptionWhenServiceThrowsError() throws Exception {
            // Given
            when(ticketService.findAllTickets()).thenThrow(new RuntimeException("Service Error"));

            // When & Then
            mockMvc.perform(get("/tickets")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Get Ticket By Id Tests")
    class GetTicketByIdTests {

        @Test
        @DisplayName("Should return ticket when it exists")
        void shouldReturnTicketWhenItExists() throws Exception {
            // Given
            Long ticketId = 1L;
            TicketDto ticketDto = new TicketDto(ticketId, "Test Ticket", "Description", StatusTicket.PENDING);
            when(ticketService.findTicketById(ticketId)).thenReturn(ticketDto);

            // When & Then
            mockMvc.perform(get("/tickets/{id}", ticketId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(ticketId))
                    .andExpect(jsonPath("$.title").value("Test Ticket"))
                    .andExpect(jsonPath("$.description").value("Description"))
                    .andExpect(jsonPath("$.status").value("PENDING"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when ticket doesn't exist")
        void shouldReturn404WhenTicketDoesNotExist() throws Exception {
            // Given
            Long ticketId = 999L;
            when(ticketService.findTicketById(ticketId)).thenThrow(new TicketNotFoundException("Ticket not found with userId: " + ticketId));

            // When & Then
            mockMvc.perform(get("/tickets/{id}", ticketId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should handle internal server error")
        void shouldHandleInternalServerError() throws Exception {
            // Given
            Long ticketId = 1L;
            when(ticketService.findTicketById(ticketId)).thenThrow(new RuntimeException("Internal Server Error"));

            // When & Then
            mockMvc.perform(get("/tickets/{id}", ticketId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    @DisplayName("Update Ticket Tests")
    class UpdateTicketTests {

        @Test
        @DisplayName("Should update ticket successfully")
        void shouldUpdateTicketSuccessfully() throws Exception {
            // Given
            Long ticketId = 1L;
            TicketDto inputDto = new TicketDto(null, "Updated Ticket", "Updated Description", StatusTicket.DONE);
            TicketDto outputDto = new TicketDto(ticketId, "Updated Ticket", "Updated Description", StatusTicket.DONE);

            when(ticketService.modifyTicket(eq(ticketId), any(TicketDto.class))).thenReturn(outputDto);

            // When & Then
            mockMvc.perform(put("/tickets/{id}", ticketId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(ticketId))
                    .andExpect(jsonPath("$.title").value("Updated Ticket"))
                    .andExpect(jsonPath("$.description").value("Updated Description"))
                    .andExpect(jsonPath("$.status").value("DONE"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when updating non-existent ticket")
        void shouldReturn404WhenUpdatingNonExistentTicket() throws Exception {
            // Given
            Long ticketId = 999L;
            TicketDto inputDto = new TicketDto(null, "Updated Ticket", "Updated Description", StatusTicket.DONE);

            when(ticketService.modifyTicket(eq(ticketId), any(TicketDto.class)))
                    .thenThrow(new TicketNotFoundException("Ticket not found with userId: " + ticketId));

            // When & Then
            mockMvc.perform(put("/tickets/{id}", ticketId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Delete Ticket Tests")
    class DeleteTicketTests {

        @Test
        @DisplayName("Should delete ticket successfully")
        void shouldDeleteTicketSuccessfully() throws Exception {
            // Given
            Long ticketId = 1L;
            doNothing().when(ticketService).deleteTicket(ticketId);

            // When & Then
            mockMvc.perform(delete("/tickets/{id}", ticketId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            verify(ticketService, times(1)).deleteTicket(ticketId);
        }

        @Test
        @DisplayName("Should return 404 Not Found when ticket doesn't exist")
        void shouldReturn404WhenTicketDoesNotExist() throws Exception {
            // Given
            Long ticketId = 999L;
            doThrow(new TicketNotFoundException("Ticket not found with userId: " + ticketId))
                    .when(ticketService).deleteTicket(ticketId);

            // When & Then
            mockMvc.perform(delete("/tickets/{id}", ticketId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());

            verify(ticketService, times(1)).deleteTicket(ticketId);
        }

        @Test
        @DisplayName("Should handle internal server error")
        void shouldHandleInternalServerError() throws Exception {
            // Given
            Long ticketId = 1L;
            doThrow(new RuntimeException("Internal Server Error"))
                    .when(ticketService).deleteTicket(ticketId);

            // When & Then
            mockMvc.perform(delete("/tickets/{id}", ticketId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());

            verify(ticketService, times(1)).deleteTicket(ticketId);
        }
    }

    @Nested
    @DisplayName("Assign Ticket Tests")
    class AssignTicketTests {

        @Test
        @DisplayName("Should assign ticket successfully")
        void shouldAssignTicketSuccessfully() throws Exception {
            // Given
            Long ticketId = 1L;
            Long userId = 2L;
            TicketDto ticketDto = new TicketDto(ticketId, "Test Ticket", "Description", StatusTicket.PENDING);
            AssignedDto assignedDto = new AssignedDto(userId, "testuser", "test@example.com", ticketDto);

            when(ticketService.assignTicket(ticketId, userId)).thenReturn(assignedDto);

            // When & Then
            mockMvc.perform(put("/tickets/{id}/assign/{userId}", ticketId, userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.userId").value(userId))
                    .andExpect(jsonPath("$.username").value("testuser"))
                    .andExpect(jsonPath("$.email").value("test@example.com"))
                    .andExpect(jsonPath("$.tickets.id").value(ticketId))
                    .andExpect(jsonPath("$.tickets.title").value("Test Ticket"))
                    .andExpect(jsonPath("$.tickets.status").value("PENDING"));
        }

        @Test
        @DisplayName("Should return 404 Not Found when ticket doesn't exist")
        void shouldReturn404WhenTicketDoesNotExist() throws Exception {
            // Given
            Long ticketId = 999L;
            Long userId = 2L;

            when(ticketService.assignTicket(ticketId, userId))
                    .thenThrow(new TicketNotFoundException("Ticket not found: " + ticketId));

            // When & Then
            mockMvc.perform(put("/tickets/{id}/assign/{userId}", ticketId, userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 404 Not Found when user doesn't exist")
        void shouldReturn404WhenUserDoesNotExist() throws Exception {
            // Given
            Long ticketId = 1L;
            Long userId = 999L;

            when(ticketService.assignTicket(ticketId, userId))
                    .thenThrow(new UserNotFoundException("User not found: " + userId));

            // When & Then
            mockMvc.perform(put("/tickets/{id}/assign/{userId}", ticketId, userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should handle internal server error")
        void shouldHandleInternalServerError() throws Exception {
            // Given
            Long ticketId = 1L;
            Long userId = 2L;

            when(ticketService.assignTicket(anyLong(), anyLong()))
                    .thenThrow(new RuntimeException("Internal Server Error"));

            // When & Then
            mockMvc.perform(put("/tickets/{id}/assign/{userId}", ticketId, userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        }
    }
}
