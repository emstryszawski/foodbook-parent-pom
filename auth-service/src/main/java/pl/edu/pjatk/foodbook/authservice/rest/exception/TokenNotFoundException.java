package pl.edu.pjatk.foodbook.authservice.rest.exception;

public class TokenNotFoundException extends RuntimeException {

    public TokenNotFoundException(String message) {
        super(message);
    }
}
