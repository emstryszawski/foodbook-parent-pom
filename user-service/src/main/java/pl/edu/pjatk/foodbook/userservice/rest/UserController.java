package pl.edu.pjatk.foodbook.userservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.foodbook.userservice.exception.NotFoundException;
import pl.edu.pjatk.foodbook.userservice.repository.model.User;
import pl.edu.pjatk.foodbook.userservice.rest.dto.NewRequestUser;
import pl.edu.pjatk.foodbook.userservice.rest.service.UserService;

@RestController
@RequestMapping(
    name = "User API",
    path = "/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.getUser(username));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveUser(@RequestBody @Valid NewRequestUser user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
