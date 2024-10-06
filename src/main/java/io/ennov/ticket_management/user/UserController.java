package io.ennov.ticket_management.user;

import io.ennov.ticket_management.ticket.TicketDto;
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

    @GetMapping("/{id}/ticket")
    public ResponseEntity<List<TicketDto>> getUserById(
            @PathVariable Long id
    ) {
        List<TicketDto> ticketList = userService.findTicketByUser(id);
        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userList = userService.findAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserDto userDto
    ) {
        UserDto newUser = userService.modifyUser(id, userDto);
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
