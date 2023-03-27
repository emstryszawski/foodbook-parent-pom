package pl.edu.pjatk.foodbook.userservice.rest;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.pjatk.foodbook.userservice.exception.AlreadyExistsException;

import java.util.Collections;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        ApiError error = ApiError.builder()
                             .status(exception.getStatusCode())
                             .message("Invalid request body.")
                             .errors(exception.getBindingResult().getFieldErrors().stream()
                                         .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                         .collect(Collectors.toList()))
                             .build();
        return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(AlreadyExistsException exception) {
        ApiError error = ApiError.builder()
                             .status(exception.getStatusCode())
                             .message(exception.getExistingProperty() + " is taken.")
                             .errors(Collections.singletonList(exception.getLocalizedMessage()))
                             .build();
        return new ResponseEntity<>(error, new HttpHeaders(), error.getStatus());
    }
}
