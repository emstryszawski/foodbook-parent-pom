package pl.edu.pjatk.foodbook.authservice.rest.service;

import org.springframework.security.core.userdetails.UserDetails;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.User;

import java.util.UUID;

public interface TokenService {

    void saveUserToken(UUID userId, String jwtToken);

    void revokeUserTokens(UUID userId);

    boolean isTokenValid(String jwtToken, UserDetails userDetails);

    String extractUsername(String jwtToken);

    String generateToken(User user);
}
