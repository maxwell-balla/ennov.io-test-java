package io.ennov.ticket_management.service;

import io.ennov.ticket_management.domain.User;
import io.ennov.ticket_management.dto.UserDto;
import io.ennov.ticket_management.repository.UserRepository;
import io.ennov.ticket_management.domain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserDto createUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());

        User newUser = userRepository.save(user);
        return userMapper.userToUserDto(newUser);
    }
}
