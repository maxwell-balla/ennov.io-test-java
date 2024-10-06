package io.ennov.ticket_management.controller;

import io.ennov.ticket_management.dto.TicketDto;
import io.ennov.ticket_management.dto.UserDto;
import io.ennov.ticket_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{userId}/ticket")
    public ResponseEntity<List<TicketDto>> getUserById(
            @PathVariable Long userId
    ) {
        List<TicketDto> ticketList = userService.findTicketByUser(userId);
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }
}
