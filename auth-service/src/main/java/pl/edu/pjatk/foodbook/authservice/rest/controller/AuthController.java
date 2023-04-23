package pl.edu.pjatk.foodbook.authservice.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.foodbook.authservice.repository.model.AccessToken;
import pl.edu.pjatk.foodbook.authservice.repository.model.RefreshToken;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationRequest;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationResponse;
import pl.edu.pjatk.foodbook.authservice.rest.dto.RefreshTokenRequest;
import pl.edu.pjatk.foodbook.authservice.rest.dto.RegisterRequest;
import pl.edu.pjatk.foodbook.authservice.rest.service.AuthenticationService;
import pl.edu.pjatk.foodbook.authservice.rest.service.JwtTokenService;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.User;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    private final JwtTokenService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
        @RequestBody @Valid RegisterRequest request
    ) {
        User registeredUser = authService.register(request);
        Pair<AccessToken, RefreshToken> tokens = jwtService.generateTokens(registeredUser);
        return ResponseEntity.ok(buildAuthenticationResponse(registeredUser, tokens));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ) {
        User authenticatedUser = authService.authenticate(request);
        Pair<AccessToken, RefreshToken> tokens = jwtService.generateTokens(authenticatedUser);
        return ResponseEntity.ok(buildAuthenticationResponse(authenticatedUser, tokens));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
        @RequestBody RefreshTokenRequest refreshRequest
    ) {
        Pair<AccessToken, RefreshToken> tokens = jwtService.refreshTokens(refreshRequest.getRefreshToken());
        AccessToken accessToken = tokens.getFirst();
        RefreshToken refreshToken = tokens.getSecond();
        return ResponseEntity.ok(
            AuthenticationResponse.builder()
                .token(accessToken.getToken())
                .tokenExpiresIn(accessToken.expiresIn())
                .refreshToken(refreshToken.getToken().toString())
                .refreshTokenExpiresIn(refreshToken.expiresIn())
                .build()
        );
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(
        @RequestParam(value = "token") String token
    ) {
        return ResponseEntity.ok(jwtService.isTokenValid(token));
    }

    private AuthenticationResponse buildAuthenticationResponse(User registeredUser, Pair<AccessToken, RefreshToken> tokens) {
        AccessToken accessToken = tokens.getFirst();
        RefreshToken refreshToken = tokens.getSecond();
        return
            AuthenticationResponse.builder()
                .id(registeredUser.getId())
                .email(registeredUser.getEmail())
                .username(registeredUser.getUsername())
                .token(accessToken.getToken())
                .tokenExpiresIn(accessToken.expiresIn())
                .refreshToken(refreshToken.getToken().toString())
                .refreshTokenExpiresIn(refreshToken.expiresIn())
                .build();
    }
}
