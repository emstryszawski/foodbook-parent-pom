package pl.edu.pjatk.foodbook.authservice.rest.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.authservice.repository.model.AccessToken;
import pl.edu.pjatk.foodbook.authservice.repository.model.RefreshToken;
import pl.edu.pjatk.foodbook.authservice.rest.exception.TokenNotFoundException;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.GrantedAuthority;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.User;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtTokenService {
    private final int TOKEN_EXPIRATION_TIME;
    private final int REFRESH_TOKEN_EXPIRATION_TIME;
    private final TokenService tokenService;
    private final Key signInKey;

    public JwtTokenService(
        @Value("${foodbook.security.jwt.tokenExpirationTimeMs}") int tokenExpirationTime,
        @Value("${foodbook.security.jwt.refreshTokenExpirationTimeMs}") int refreshTokenExpirationTime,
        TokenService tokenService, Key signInKey) {
        TOKEN_EXPIRATION_TIME = tokenExpirationTime;
        REFRESH_TOKEN_EXPIRATION_TIME = refreshTokenExpirationTime;
        this.tokenService = tokenService;
        this.signInKey = signInKey;
    }

    public boolean isTokenValid(String jwt) {
        AccessToken token = tokenService.getToken(jwt);
        return token.isValid();
    }

    public Pair<AccessToken, RefreshToken> generateTokens(User user) {
        ZonedDateTime tokenValid = ZonedDateTime.now(ZoneId.systemDefault()).plus(TOKEN_EXPIRATION_TIME, ChronoUnit.MILLIS);
        LocalDateTime accessTokenValidUntil = tokenValid.toLocalDateTime();
        LocalDateTime refreshTokenValidUntil = ZonedDateTime.now(ZoneId.systemDefault()).plus(REFRESH_TOKEN_EXPIRATION_TIME, ChronoUnit.MILLIS).toLocalDateTime();

        String username = user.getUsername();
        UUID userId = user.getId();
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", user.getAuthorities());
        String jwt = generateJwtToken(username, tokenValid.toInstant(), claims);

        tokenService.revokeAll(userId);

        AccessToken accessToken = AccessToken.builder()
            .userId(userId)
            .token(jwt)
            .build();

        accessToken.setValidUntil(accessTokenValidUntil);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setValidUntil(refreshTokenValidUntil);

        accessToken.setRefreshToken(refreshToken);

        return tokenService.save(accessToken);
    }

    private String generateJwtToken(String subject, Instant expirationDate, Map<String, ?> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuer("foodbook-auth-service")
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(Date.from(expirationDate))
            .signWith(signInKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public Pair<AccessToken, RefreshToken> refreshTokens(UUID refreshTokenId) {
        RefreshToken refreshToken = tokenService.getRefreshToken(refreshTokenId);

        if (!refreshToken.isValid()) {
            throw new TokenNotFoundException("Refresh token is invalid");
        }

        AccessToken accessToken = tokenService.getAccessTokenByRefreshToken(refreshToken);
        UUID userId = accessToken.getUserId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<GrantedAuthority> authorities = authentication.getAuthorities()
            .stream()
            .map(grantedAuthority -> new GrantedAuthority().authority(grantedAuthority.getAuthority()))
            .collect(Collectors.toList());

        log.info("username {}", username);

        return generateTokens(
            new User()
                .id(userId)
                .username(username)
                .authorities(authorities));
    }

    public void removeAll(String jwt) {
        tokenService.removeAll(jwt);
    }
}
