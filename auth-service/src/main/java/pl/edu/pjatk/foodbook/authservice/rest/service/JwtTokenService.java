package pl.edu.pjatk.foodbook.authservice.rest.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.authservice.repository.RefreshTokenRepository;
import pl.edu.pjatk.foodbook.authservice.repository.model.RefreshToken;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationResponse;
import pl.edu.pjatk.foodbook.authservice.rest.exception.InvalidTokenException;
import pl.edu.pjatk.foodbook.authservice.rest.exception.TokenNotFoundException;
import pl.edu.pjatk.foodbook.authservice.security.JwtHelper;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
public class JwtTokenService {
    private final int TOKEN_EXPIRATION_TIME;
    private final int REFRESH_TOKEN_EXPIRATION_TIME;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtHelper jwtHelper;
    private final Key signInKey;

    public JwtTokenService(
        @Value("${foodbook.security.jwt.tokenExpirationTimeMs}") int tokenExpirationTime,
        @Value("${foodbook.security.jwt.refreshTokenExpirationTimeMs}") int refreshTokenExpirationTime,
        UserDetailsService userDetailsService,
        RefreshTokenRepository refreshTokenRepository, JwtHelper jwtHelper,
        Key signInKey) {
        TOKEN_EXPIRATION_TIME = tokenExpirationTime;
        REFRESH_TOKEN_EXPIRATION_TIME = refreshTokenExpirationTime;
        this.userDetailsService = userDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtHelper = jwtHelper;
        this.signInKey = signInKey;
    }

    public AuthenticationResponse generateTokens(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities());
        String jwt = generateJwtToken(username, claims);

        RefreshToken refreshToken = RefreshToken.builder()
            .username(username)
            .expirationDate(LocalDateTime.now().plus(REFRESH_TOKEN_EXPIRATION_TIME, ChronoUnit.MILLIS))
            .build();

        revokeAllRefreshTokens(username);

        refreshTokenRepository.save(refreshToken);

        return AuthenticationResponse.builder()
            .token(jwt)
            .tokenExpiresIn(Duration.between(Instant.now(), jwtHelper.extractExpirationTime(jwt).toInstant()).toMillis())
            .refreshToken(refreshToken.getId().toString())
            .refreshTokenExpiresIn(Duration.between(Instant.now(),
                refreshToken.getExpirationDate()
                    .atZone(ZoneId.systemDefault())
                    .toInstant()).toMillis())
            .build();
    }

    private String generateJwtToken(String subject, Map<String, ?> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuer("foodbook-auth-service")
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
            .signWith(signInKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public AuthenticationResponse refreshTokens(UUID refreshTokenId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenId)
            .orElseThrow(() -> new TokenNotFoundException("Refresh token was not found"));

        if (!refreshToken.isValid()) {
            throw new InvalidTokenException("Refresh token is invalid");
        }

        String username = refreshToken.getUsername();

        return generateTokens(username);
    }

    public boolean isTokenValid(String jwtToken) {
        try {
            String username = jwtHelper.extractUsername(jwtToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return username.equals(userDetails.getUsername()) && jwtHelper.isTokenNonExpired(jwtToken);
        } catch (InvalidTokenException e) {
            return false;
        }
    }

    public void revokeAllRefreshTokens(String username) {
        List<RefreshToken> revokedRefreshTokens = refreshTokenRepository.findAllByUsername(username)
            .stream()
            .peek(token -> token.setRevoked(true))
            .toList();

        refreshTokenRepository.saveAllAndFlush(revokedRefreshTokens);
    }
}
