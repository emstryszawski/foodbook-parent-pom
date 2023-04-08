package pl.edu.pjatk.foodbook.authservice.rest.exception;

public class InvalidRefreshToken extends RuntimeException {
    public InvalidRefreshToken() {
        super("Refresh token is not valid");
    }
}
