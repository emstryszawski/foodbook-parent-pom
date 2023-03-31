package pl.edu.pjatk.foodbook.authservice.rest.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.authservice.repository.TokenRepository;
import pl.edu.pjatk.foodbook.authservice.repository.model.Token;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.User;

import java.io.IOException;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService implements TokenService, LogoutHandler {

    private final String PUBLIC_KEY;

    private final int TOKEN_EXPIRATION_TIME;

    private final TokenRepository tokenRepository;

    @Autowired
    public JwtService(
        @Value("${token.key}") String publicKey,
        @Value("${token.expiration_time}") int tokenExpirationTime,
        TokenRepository tokenRepository) {
        PUBLIC_KEY = publicKey;
        TOKEN_EXPIRATION_TIME = tokenExpirationTime;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void saveUserToken(UUID userId, String jwtToken) {
        Token token = Token.builder()
                          .token(jwtToken)
                          .userId(userId)
                          .expired(false)
                          .revoked(false)
                          .build();

        tokenRepository.save(token);
    }

    @Override
    public void revokeUserTokens(UUID userId) {
        List<Token> tokens = tokenRepository.findValidTokensByUserId(userId).stream()
                                 .peek(token -> {
                                     token.setExpired(true);
                                     token.setRevoked(true);
                                 })
                                 .collect(Collectors.toList());
        tokenRepository.saveAll(tokens);
    }

    @Override
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken) && isTokenValid(jwtToken);
    }

    private boolean isTokenValid(String jwtToken) {
        return tokenRepository.findByToken(jwtToken)
                   .filter(token -> !token.isExpired() && !token.revoked)
                   .isPresent();
    }

    @Override
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder()
                   .setSigningKey(getSignInKey())
                   .build()
                   .parseClaimsJws(jwtToken)
                   .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(PUBLIC_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    private String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts.builder()
                   .setClaims(extraClaims)
                   .setSubject(user.getUsername())
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                   .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) {

        String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            returnUnauthorizedResponse(response);
            return;
        }
        jwtToken = authHeader.substring(7);
        if (isTokenValid(jwtToken)) {
            tokenRepository.findByToken(jwtToken).ifPresent(storedToken -> {
                storedToken.setExpired(true);
                storedToken.setRevoked(true);
                tokenRepository.save(storedToken);
                SecurityContextHolder.clearContext();
            });
        } else {
            returnUnauthorizedResponse(response);
        }
    }

    private static void returnUnauthorizedResponse(HttpServletResponse response) {
        try {
            response.sendError(401, "Invalid or no jwt token");
        } catch (IOException ignored) {

        }
    }
}
