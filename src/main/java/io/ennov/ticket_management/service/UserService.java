package io.ennov.ticket_management.service;

import io.ennov.ticket_management.domain.User;
import io.ennov.ticket_management.domain.mapper.TicketMapper;
import io.ennov.ticket_management.dto.TicketDto;
import io.ennov.ticket_management.dto.UserDto;
import io.ennov.ticket_management.exception.EmailAlreadyExistsException;
import io.ennov.ticket_management.exception.UserNotFoundException;
import io.ennov.ticket_management.exception.UsernameAlreadyExistsException;
import io.ennov.ticket_management.repository.TicketRepository;
import io.ennov.ticket_management.repository.UserRepository;
import io.ennov.ticket_management.domain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final UserMapper userMapper;
    private final TicketMapper ticketMapper;

    @Transactional
    public UserDto createUser(UserDto dto) {
        validateNewUser(dto);
        User user = userMapper.userDtoToUser(dto);
        User savedUser = userRepository.save(user);
        return userMapper.userToUserDto(savedUser);
    }

    private void validateNewUser(UserDto dto) {
        usernameExists(dto.username());
        emailExists(dto.email());
    }

    void emailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email already exists: " + email);
        }
    }

    void usernameExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already exists: " + username);
        }
    }

    public List<TicketDto> findTicketByUser(Long userId) {
        verifiedUser(userId);

        return ticketRepository.findAllByUserId(userId)
                .stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void verifiedUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public UserDto modifyUser(Long userId, UserDto userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        User newUser = mergeToNewUser(existingUser, userDto);
        userRepository.save(newUser);
        return userMapper.userToUserDto(newUser);
    }

    private User mergeToNewUser(User existingUser, UserDto userDto) {
        if (userDto.username() != null && !existingUser.getUsername().equals(userDto.username())) {
            usernameExists(userDto.username());
            existingUser.setUsername(userDto.username());
        }
        if (userDto.email() != null && !existingUser.getEmail().equals(userDto.email())) {
            emailExists(userDto.email());
            existingUser.setEmail(userDto.email());
        }
        return existingUser;
    }

    public void deleteUser(Long userId) {
        verifiedUser(userId);
        userRepository.deleteById(userId);
    }
}
