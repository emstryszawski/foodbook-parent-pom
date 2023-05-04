package pl.edu.pjatk.foodbook.recipeservice.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.edu.pjatk.foodbook.recipeservice.rest.dto.ApiError;
import pl.edu.pjatk.foodbook.recipeservice.rest.exception.RecipeNotFoundException;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@RestControllerAdvice
public class RecipeExceptionHandler {

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ApiError> handleRecipeNotFound(RecipeNotFoundException e) {
        ApiError apiError = ApiError.builder()
            .timestamp(Date.from(Instant.now()))
            .code(404)
            .status(HttpStatus.NOT_FOUND.name())
            .message(e.getLocalizedMessage())
            .errors(Collections.singletonList(e.getLocalizedMessage()))
            .build();
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
