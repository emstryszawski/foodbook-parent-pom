package pl.edu.pjatk.foodbook.authservice.rest.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    @Nullable
    private UUID id;
    @Nullable
    private String email;
    @Nullable
    private String username;
    @NotNull
    private String token;
    @NotNull
    private Long tokenExpiresIn;
    @NotNull
    private String refreshToken;
    @NotNull
    private Long refreshTokenExpiresIn;
}
