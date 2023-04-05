package pl.edu.pjatk.foodbook.gatewayservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.security.Key;

@Slf4j
@Service
public class TokenService {

    private final Key key;

    public TokenService(@Value("${secret}") String key) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
    }

    @Nullable
    public String extractToken(@NotNull ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").stream()
            .filter(StringUtils::isNotEmpty)
            .filter(bearerToken -> bearerToken.startsWith("Bearer "))
            .map(bearerToken -> bearerToken.substring(7))
            .findFirst()
            .orElse(null);
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT Claims are empty");
        }
        return false;
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
