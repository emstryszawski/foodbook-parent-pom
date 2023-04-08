package pl.edu.pjatk.foodbook.authservice.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        return ResponseEntity.ok(
            AuthenticationResponse.builder()
                .token(tokens.getFirst().getToken()) // TODO possible recursive dependency on refresh token
                .refreshToken(tokens.getSecond().getToken().toString())
                .build()
        );
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ) {
        User authenticatedUser = authService.authenticate(request);
        Pair<AccessToken, RefreshToken> tokens = jwtService.generateTokens(authenticatedUser);
        return ResponseEntity.ok(
            AuthenticationResponse.builder()
                .token(tokens.getFirst().getToken()) // TODO possible recursive dependency on refresh token
                .refreshToken(tokens.getSecond().getToken().toString())
                .build()
        );
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
        @RequestBody RefreshTokenRequest refreshRequest
    ) {
        Pair<AccessToken, RefreshToken> tokens = jwtService.refreshTokens(refreshRequest.getRefreshToken());
        return ResponseEntity.ok(
            AuthenticationResponse.builder()
                .token(tokens.getFirst().getToken())
                .refreshToken(tokens.getSecond().getToken().toString())
                .build()
        );
    }
}
