package pl.edu.pjatk.foodbook.userservice.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.pjatk.foodbook.userservice.exception.UserAlreadyExistsException;
import pl.edu.pjatk.foodbook.userservice.exception.UserNotFoundException;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@RestControllerAdvice
public class UserControllerExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(UserAlreadyExistsException exception) {
        ApiError error = ApiError.builder()
                             .timestamp(Date.from(Instant.now(Clock.systemDefaultZone())))
                             .code(exception.getStatusCode().value())
                             .status(exception.getStatusCode().toString())
                             .message(exception.getExistingProperty() + " is taken.")
                             .errors(Collections.singletonList(exception.getLocalizedMessage()))
                             .build();
        return new ResponseEntity<>(error, new HttpHeaders(), error.getCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException exception) {
        ApiError error = ApiError.builder()
            .timestamp(Date.from(Instant.now(Clock.systemDefaultZone())))
            .code(404)
            .status(HttpStatus.NOT_FOUND.name())
            .message(exception.getMessage())
            .errors(Collections.singletonList(exception.getMessage()))
            .build();
        return new ResponseEntity<>(error, new HttpHeaders(), error.getCode());
    }
}
