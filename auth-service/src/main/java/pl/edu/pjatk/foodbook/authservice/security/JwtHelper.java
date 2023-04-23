package pl.edu.pjatk.foodbook.authservice.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Slf4j
@Component
public class JwtHelper {

    private static final String BEARER = "Bearer ";

    private final Key signInKey;


    public JwtHelper(Key signInKey) {
        this.signInKey = signInKey;
    }

    public String getJwtToken(String authHeader) {
        return authHeader.substring(7);
    }

    public boolean isAuthHeaderValid(String authHeader) {
        return authHeader != null && authHeader.startsWith(BEARER);
    }

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    public String extractUsernameFromToken(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(signInKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT Claims are empty");
        }
        return null;
    }
}
