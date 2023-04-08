package pl.edu.pjatk.foodbook.authservice.rest.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RefreshTokenRequest {
    private UUID refreshToken;
}
