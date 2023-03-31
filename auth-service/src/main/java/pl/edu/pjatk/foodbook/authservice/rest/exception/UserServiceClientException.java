package pl.edu.pjatk.foodbook.authservice.rest.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import pl.edu.pjatk.foodbook.authservice.rest.ApiError;

public class UserServiceClientException extends RuntimeException {

    private ApiError error;

    public UserServiceClientException(String message) {
        super(message);
        try {
            ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
            error = objectMapper.readValue(message, ApiError.class);
        } catch (JsonProcessingException e) {
            error = new ApiError();
        }
    }

    public UserServiceClientException(ApiError error, String message) {
        super(message);
        this.error = error;
    }

    public ApiError getError() {
        return error;
    }
}
