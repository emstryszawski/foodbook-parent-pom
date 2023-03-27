package pl.edu.pjatk.foodbook.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class AlreadyExistsException extends Exception implements ErrorResponse {

    private final ProblemDetail body;

    private final String existingProperty;

    public AlreadyExistsException(String message, String existingProperty) {
        super(message);
        this.existingProperty = existingProperty;
        this.body = ProblemDetail.forStatusAndDetail(getStatusCode(), "Invalid request body.");
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public ProblemDetail getBody() {
        return this.body;
    }

    public String getExistingProperty() {
        return existingProperty;
    }
}
