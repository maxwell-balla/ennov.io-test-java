package io.ennov.ticket_management.controller;

import io.ennov.ticket_management.dto.UserDto;
import io.ennov.ticket_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> addUser(
            @RequestBody @Valid UserDto userDto
    ) {
        UserDto newUser = userService.createUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}
