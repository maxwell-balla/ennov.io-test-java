package io.ennov.ticket_management.unities.service;

import io.ennov.ticket_management.domain.StatusTicket;
import io.ennov.ticket_management.domain.Ticket;
import io.ennov.ticket_management.domain.User;
import io.ennov.ticket_management.domain.mapper.TicketMapper;
import io.ennov.ticket_management.domain.mapper.UserMapper;
import io.ennov.ticket_management.dto.TicketDto;
import io.ennov.ticket_management.dto.UserDto;
import io.ennov.ticket_management.exception.EmailAlreadyExistsException;
import io.ennov.ticket_management.exception.UserNotFoundException;
import io.ennov.ticket_management.exception.UsernameAlreadyExistsException;
import io.ennov.ticket_management.repository.TicketRepository;
import io.ennov.ticket_management.repository.UserRepository;
import io.ennov.ticket_management.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("Create User Tests")
    class CreateUserTests {
        private UserDto inputDto;
        private User user;
        private User savedUser;
        private UserDto outputDto;

        @BeforeEach
        void setUp() {
            inputDto = new UserDto(null, "testuser", "test@example.com");
            user = new User();
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            savedUser = new User();
            savedUser.setId(1L);
            savedUser.setUsername("testuser");
            savedUser.setEmail("test@example.com");
            outputDto = new UserDto(1L, "testuser", "test@example.com");
        }

        @Test
        @DisplayName("Should create a new user when input is valid")
        void shouldCreateNewUserWhenInputIsValid() {
            when(userRepository.existsByUsername(inputDto.username())).thenReturn(false);
            when(userRepository.existsByEmail(inputDto.email())).thenReturn(false);
            when(userMapper.userDtoToUser(inputDto)).thenReturn(user);
            when(userRepository.save(user)).thenReturn(savedUser);
            when(userMapper.userToUserDto(savedUser)).thenReturn(outputDto);

            UserDto result = userService.createUser(inputDto);

            assertThat(result).isEqualTo(outputDto);
            verify(userRepository).existsByUsername(inputDto.username());
            verify(userRepository).existsByEmail(inputDto.email());
            verify(userMapper).userDtoToUser(inputDto);
            verify(userRepository).save(user);
            verify(userMapper).userToUserDto(savedUser);
        }

        @Test
        @DisplayName("Should throw UsernameAlreadyExistsException when username exists")
        void shouldThrowUsernameAlreadyExistsExceptionWhenUsernameExists() {
            when(userRepository.existsByUsername(inputDto.username())).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(inputDto))
                    .isInstanceOf(UsernameAlreadyExistsException.class)
                    .hasMessageContaining("Username already exists: " + inputDto.username());

            verify(userRepository).existsByUsername(inputDto.username());
            verify(userRepository, never()).existsByEmail(any());
            verify(userMapper, never()).userDtoToUser(any());
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw EmailAlreadyExistsException when email exists")
        void shouldThrowEmailAlreadyExistsExceptionWhenEmailExists() {
            when(userRepository.existsByUsername(inputDto.username())).thenReturn(false);
            when(userRepository.existsByEmail(inputDto.email())).thenReturn(true);

            assertThatThrownBy(() -> userService.createUser(inputDto))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessageContaining("Email already exists: " + inputDto.email());

            verify(userRepository).existsByUsername(inputDto.username());
            verify(userRepository).existsByEmail(inputDto.email());
            verify(userMapper, never()).userDtoToUser(any());
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Find Tickets By User Tests")
    class FindTicketsByUserTests {
        private Long userId;
        private User user;
        private List<Ticket> ticket;
        private List<TicketDto> ticketDto;

        @BeforeEach
        void setUp() {
            userId = 1L;
            user = new User();
            user.setId(userId);
            user.setUsername("testuser");
            user.setEmail("test@example.com");

            ticket = List.of(
                    new Ticket(1L, "Ticket 1", "Description 1", StatusTicket.PENDING, user),
                    new Ticket(2L, "Ticket 2", "Description 2", StatusTicket.DONE, user)
            );

            ticketDto = List.of(
                    new TicketDto(1L, "Ticket 1", "Description 1", StatusTicket.PENDING, userId),
                    new TicketDto(2L, "Ticket 2", "Description 2", StatusTicket.DONE, userId)
            );
        }

        @Test
        @DisplayName("Should return list of tickets when user exists")
        void shouldReturnListOfTicketsWhenUserExists() {
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(ticketRepository.findAllByUserId(userId)).thenReturn(ticket);
            when(ticketMapper.ticketToTicketDto(any(Ticket.class))).thenReturn(ticketDto.get(0), ticketDto.get(1));

            List<TicketDto> result = userService.findTicketByUser(userId);

            assertThat(result).hasSize(2).isEqualTo(ticketDto);
            verify(userRepository).findById(userId);
            verify(ticketRepository).findAllByUserId(userId);
            verify(ticketMapper, times(2)).ticketToTicketDto(any(Ticket.class));
        }

        @Test
        @DisplayName("Should return empty list when user has no tickets")
        void shouldReturnEmptyListWhenUserHasNoTickets() {
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(ticketRepository.findAllByUserId(userId)).thenReturn(List.of());

            List<TicketDto> result = userService.findTicketByUser(userId);

            assertThat(result).isEmpty();
            verify(userRepository).findById(userId);
            verify(ticketRepository).findAllByUserId(userId);
            verify(ticketMapper, never()).ticketToTicketDto(any(Ticket.class));
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user does not exist")
        void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.findTicketByUser(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User not found: " + userId);

            verify(userRepository).findById(userId);
            verify(ticketRepository, never()).findAllByUserId(any());
            verify(ticketMapper, never()).ticketToTicketDto(any());
        }
    }
}
