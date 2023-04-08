package pl.edu.pjatk.foodbook.authservice.rest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pjatk.foodbook.authservice.repository.AccessTokenRepository;
import pl.edu.pjatk.foodbook.authservice.repository.RefreshTokenRepository;
import pl.edu.pjatk.foodbook.authservice.repository.model.AccessToken;
import pl.edu.pjatk.foodbook.authservice.repository.model.RefreshToken;
import pl.edu.pjatk.foodbook.authservice.repository.model.Token;
import pl.edu.pjatk.foodbook.authservice.rest.exception.TokenNotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AccessTokenRepository accessTokenRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public Pair<AccessToken, RefreshToken> save(AccessToken accessToken) {
        RefreshToken refreshToken = refreshTokenRepository.saveAndFlush(accessToken.getRefreshToken());
        accessTokenRepository.saveAndFlush(accessToken);
        return Pair.of(accessToken, refreshToken);
    }

    @Transactional
    public RefreshToken getRefreshToken(UUID refreshTokenId) {
        return refreshTokenRepository.findById(refreshTokenId)
            .orElseThrow(() -> new TokenNotFoundException("No refresh token found"));
    }

    @Transactional
    public void revokeAll(UUID userId) {
        List<AccessToken> accessTokens = accessTokenRepository
            .findByUserId(userId)
            .orElse(Collections.emptyList());
        List<AccessToken> revokedAccessTokens = accessTokens.stream()
            .filter(Token::isValid)
            .peek(accessToken -> {
                accessToken.setRevoked(true);
                accessToken.getRefreshToken().setRevoked(true);
                accessToken.setExpiredAt(LocalDateTime.now());
                accessToken.getRefreshToken().setExpiredAt(LocalDateTime.now());
            })
            .toList();
        accessTokenRepository.saveAll(revokedAccessTokens);
    }

    @Transactional
    protected AccessToken getToken(String jwt) {
        return accessTokenRepository
            .findByToken(jwt)
            .orElseThrow(() -> new TokenNotFoundException("No access token found"));
    }

    @Transactional
    public void removeAll(String jwt) {
        AccessToken accessToken = accessTokenRepository
            .findByToken(jwt)
            .orElseThrow(() -> new TokenNotFoundException("No access token found with given jwt"));
        UUID userId = accessToken.getUserId();
        accessTokenRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public AccessToken getAccessTokenByRefreshToken(RefreshToken refreshToken) {
        return accessTokenRepository.findByRefreshToken(refreshToken);
    }
}
