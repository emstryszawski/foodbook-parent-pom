package pl.edu.pjatk.foodbook.authservice.rest;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.pjatk.foodbook.authservice.rest.exception.UserServiceClientException;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@RestControllerAdvice
public class UserControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        ApiError error = ApiError.builder()
                             .timestamp(Date.from(Instant.now()))
                             .code(exception.getStatusCode().value())
                             .status(exception.getStatusCode().toString())
                             .message("Invalid request body.")
                             .errors(exception.getBindingResult().getFieldErrors().stream()
                                         .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                         .collect(Collectors.toList()))
                             .build();
        return new ResponseEntity<>(error, new HttpHeaders(), error.getCode());
    }

    @ExceptionHandler(UserServiceClientException.class)
    public ResponseEntity<ApiError> handleUserServiceClient(UserServiceClientException exception) {
        ApiError error = exception.getError();
        return new ResponseEntity<>(error, new HttpHeaders(), error.getCode());
    }
}
