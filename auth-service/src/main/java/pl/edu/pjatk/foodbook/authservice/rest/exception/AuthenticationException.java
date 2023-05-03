package pl.edu.pjatk.foodbook.authservice.rest.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
