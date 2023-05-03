package pl.edu.pjatk.foodbook.authservice.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    @NotNull
    private String token;
    @NotNull
    private Long tokenExpiresIn;
    @NotNull
    private String refreshToken;
    @NotNull
    private Long refreshTokenExpiresIn;
}
