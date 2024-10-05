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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (userRepository.existsByUsername(dto.username())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + dto.username());
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException("Email already exists: " + dto.email());
        }
    }

    public List<TicketDto> findTicketByUser(Long userId) {
        verifiedUser(userId);

        return ticketRepository.findAllByUserId(userId)
                .stream()
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());
    }

    private void verifiedUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
    }
}
