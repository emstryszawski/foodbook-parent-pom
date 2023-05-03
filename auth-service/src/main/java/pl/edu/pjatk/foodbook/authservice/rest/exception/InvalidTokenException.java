package pl.edu.pjatk.foodbook.authservice.rest.exception;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
}
