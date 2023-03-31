package pl.edu.pjatk.foodbook.userservice.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.pjatk.foodbook.userservice.exception.AlreadyExistsException;

import java.time.Clock;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@RestControllerAdvice
public class UserControllerExceptionHandler {

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(AlreadyExistsException exception) {
        ApiError error = ApiError.builder()
                             .timestamp(Date.from(Instant.now(Clock.systemDefaultZone())))
                             .code(exception.getStatusCode().value())
                             .status(exception.getStatusCode().toString())
                             .message(exception.getExistingProperty() + " is taken.")
                             .errors(Collections.singletonList(exception.getLocalizedMessage()))
                             .build();
        return new ResponseEntity<>(error, new HttpHeaders(), error.getCode());
    }
}
