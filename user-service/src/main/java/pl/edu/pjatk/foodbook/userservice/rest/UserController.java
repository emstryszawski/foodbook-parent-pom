package pl.edu.pjatk.foodbook.userservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pjatk.foodbook.userservice.rest.dto.request.CreateUserInput;
import pl.edu.pjatk.foodbook.userservice.rest.dto.response.UserRepresentation;
import pl.edu.pjatk.foodbook.userservice.rest.resource.UserResource;
import pl.edu.pjatk.foodbook.userservice.rest.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserResource {
    private final UserService userService;

    @Override
    public ResponseEntity<UserRepresentation> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @Override
    public ResponseEntity<UserRepresentation> saveUser(@RequestBody @Valid CreateUserInput user) {
        UserRepresentation response = userService.saveUser(user);
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.CREATED);
    }
}
