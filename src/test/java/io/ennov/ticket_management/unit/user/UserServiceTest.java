package io.ennov.ticket_management.unit.user;

import io.ennov.ticket_management.ticket.StatusTicket;
import io.ennov.ticket_management.ticket.Ticket;
import io.ennov.ticket_management.user.User;
import io.ennov.ticket_management.ticket.TicketMapper;
import io.ennov.ticket_management.user.UserMapper;
import io.ennov.ticket_management.ticket.TicketDto;
import io.ennov.ticket_management.user.UserDto;
import io.ennov.ticket_management.user.EmailAlreadyExistsException;
import io.ennov.ticket_management.user.UserNotFoundException;
import io.ennov.ticket_management.user.UsernameAlreadyExistsException;
import io.ennov.ticket_management.ticket.TicketRepository;
import io.ennov.ticket_management.user.UserRepository;
import io.ennov.ticket_management.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
                    new TicketDto(1L, "Ticket 1", "Description 1", StatusTicket.PENDING),
                    new TicketDto(2L, "Ticket 2", "Description 2", StatusTicket.DONE)
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

    @Nested
    @DisplayName("Find All Users Tests")
    class FindAllUsersTests {

        @Test
        @DisplayName("Should return list of all users")
        void shouldReturnListOfAllUsers() {
            // Given
            List<User> users = List.of(
                    new User(1L, "user1", "user1@example.com", new ArrayList<>()),
                    new User(2L, "user2", "user2@example.com", new ArrayList<>())
            );
            List<UserDto> userDtos = List.of(
                    new UserDto(1L, "user1", "user1@example.com"),
                    new UserDto(2L, "user2", "user2@example.com")
            );

            when(userRepository.findAll()).thenReturn(users);
            when(userMapper.userToUserDto(users.get(0))).thenReturn(userDtos.get(0));
            when(userMapper.userToUserDto(users.get(1))).thenReturn(userDtos.get(1));

            // When
            List<UserDto> result = userService.findAllUsers();

            // Then
            assertThat(result).isNotNull().hasSize(2);
            assertThat(result).containsExactlyElementsOf(userDtos);
            verify(userRepository).findAll();
            verify(userMapper, times(2)).userToUserDto(any(User.class));
        }

        @Test
        @DisplayName("Should return empty list when no users exist")
        void shouldReturnEmptyListWhenNoUsersExist() {
            // Given
            when(userRepository.findAll()).thenReturn(new ArrayList<>());

            // When
            List<UserDto> result = userService.findAllUsers();

            // Then
            assertThat(result).isNotNull().isEmpty();
            verify(userRepository).findAll();
            verify(userMapper, never()).userToUserDto(any(User.class));
        }
    }

    @Nested
    @DisplayName("Modify User Tests")
    class ModifyUserTests {

        @Test
        @DisplayName("Should update user with new username and email")
        void shouldUpdateUserWithNewUsernameAndEmail() {
            // Given
            Long userId = 1L;
            User existingUser = new User(userId, "olduser", "old@example.com", null);
            UserDto inputDto = new UserDto(null, "newuser", "new@example.com");
            User updatedUser = new User(userId, "newuser", "new@example.com", null);
            UserDto outputDto = new UserDto(userId, "newuser", "new@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(userMapper.userToUserDto(any(User.class))).thenReturn(outputDto);

            // When
            UserDto result = userService.modifyUser(userId, inputDto);

            // Then
            assertThat(result).isEqualTo(outputDto);
            verify(userRepository).findById(userId);
            verify(userRepository).save(any(User.class));
            verify(userMapper).userToUserDto(any(User.class));
        }

        @Test
        @DisplayName("Should update user with only new username")
        void shouldUpdateUserWithOnlyNewUsername() {
            // Given
            Long userId = 1L;
            User existingUser = new User(userId, "olduser", "old@example.com", null);
            UserDto inputDto = new UserDto(null, "newuser", null);
            User updatedUser = new User(userId, "newuser", "old@example.com", null);
            UserDto outputDto = new UserDto(userId, "newuser", "old@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(userMapper.userToUserDto(any(User.class))).thenReturn(outputDto);

            // When
            UserDto result = userService.modifyUser(userId, inputDto);

            // Then
            assertThat(result).isEqualTo(outputDto);
            assertThat(result.email()).isEqualTo("old@example.com");
            verify(userRepository).findById(userId);
            verify(userRepository).save(any(User.class));
            verify(userMapper).userToUserDto(any(User.class));
        }

        @Test
        @DisplayName("Should update user with only new email")
        void shouldUpdateUserWithOnlyNewEmail() {
            // Given
            Long userId = 1L;
            User existingUser = new User(userId, "olduser", "old@example.com", null);
            UserDto inputDto = new UserDto(null, null, "new@example.com");
            User updatedUser = new User(userId, "olduser", "new@example.com", null);
            UserDto outputDto = new UserDto(userId, "olduser", "new@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.save(any(User.class))).thenReturn(updatedUser);
            when(userMapper.userToUserDto(any(User.class))).thenReturn(outputDto);

            // When
            UserDto result = userService.modifyUser(userId, inputDto);

            // Then
            assertThat(result).isEqualTo(outputDto);
            assertThat(result.username()).isEqualTo("olduser");
            verify(userRepository).findById(userId);
            verify(userRepository).save(any(User.class));
            verify(userMapper).userToUserDto(any(User.class));
        }

        @Test
        @DisplayName("Should not update user when no changes are provided")
        void shouldNotUpdateUserWhenNoChangesAreProvided() {
            // Given
            Long userId = 1L;
            User existingUser = new User(userId, "olduser", "old@example.com", null);
            UserDto inputDto = new UserDto(userId, "olduser", "old@example.com");
            UserDto outputDto = new UserDto(userId, "olduser", "old@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userMapper.userToUserDto(any(User.class))).thenReturn(outputDto);

            // When
            UserDto result = userService.modifyUser(userId, inputDto);

            // Then
            assertThat(result).isEqualTo(outputDto);
            verify(userRepository).findById(userId);
            verify(userMapper).userToUserDto(any(User.class));
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user does not exist")
        void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
            // Given
            Long userId = 999L;
            UserDto inputDto = new UserDto(null, "newuser", "new@example.com");

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> userService.modifyUser(userId, inputDto))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User not found: " + userId);

            verify(userRepository).findById(userId);
            verify(userRepository, never()).save(any(User.class));
            verify(userMapper, never()).userToUserDto(any(User.class));
        }
    }

    @Nested
    @DisplayName("Delete User Tests")
    class DeleteUserTests {

        @Test
        @DisplayName("Should delete user successfully")
        void shouldDeleteUserSuccessfully() {
            // Given
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
            doNothing().when(userRepository).deleteById(userId);

            // When
            userService.deleteUser(userId);

            // Then
            verify(userRepository).findById(userId);
            verify(userRepository).deleteById(userId);
        }

        @Test
        @DisplayName("Should throw UserNotFoundException when user does not exist")
        void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
            // Given
            Long userId = 999L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When/Then
            assertThatThrownBy(() -> userService.deleteUser(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("User not found: " + userId);

            verify(userRepository).findById(userId);
            verify(userRepository, never()).deleteById(anyLong());
        }
    }
}
