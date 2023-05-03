package pl.edu.pjatk.foodbook.authservice.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationRequest;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationResponse;
import pl.edu.pjatk.foodbook.authservice.rest.dto.RefreshTokenRequest;
import pl.edu.pjatk.foodbook.authservice.rest.dto.RegisterRequest;
import pl.edu.pjatk.foodbook.authservice.rest.service.AuthenticationService;
import pl.edu.pjatk.foodbook.authservice.rest.service.JwtTokenService;

@Slf4j
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
        String username = request.getUsername();
        log.info("Request to register user with username {}", username);
        authService.register(request);
        AuthenticationResponse authenticationResponse = jwtService.generateTokens(username);
        log.info("User with username {} registered successfully", username);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ) {
        String username = request.getUsername();
        log.info("Request to authenticate user with username {}", username);
        authService.authenticate(request);
        AuthenticationResponse authenticationResponse = jwtService.generateTokens(username);
        log.info("User with username {} authenticated successfully", username);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
        @RequestBody RefreshTokenRequest refreshRequest
    ) {
        log.info("Request to refresh token with refresh token id {}", refreshRequest.getRefreshToken());
        AuthenticationResponse authenticationResponse = jwtService.refreshTokens(refreshRequest.getRefreshToken());
        log.info("Token successfully refresh");
        return ResponseEntity.ok(authenticationResponse);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(
        @RequestParam(value = "token") String token
    ) {
        return ResponseEntity.ok(jwtService.isTokenValid(token));
    }

}
