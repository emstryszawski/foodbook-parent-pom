package pl.edu.pjatk.foodbook.authservice.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import pl.edu.pjatk.foodbook.authservice.rest.service.JwtTokenService;
import pl.edu.pjatk.foodbook.authservice.security.JwtHelper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenService jwtService;
    private final JwtHelper jwtHelper;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (jwtHelper.isAuthHeaderInvalid(authHeader)) {
            try {
                response.sendError(401, "Unauthorized");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        String jwtToken = jwtHelper.getJwtToken(authHeader);
        String username = jwtHelper.extractUsername(jwtToken);
        jwtService.revokeAllRefreshTokens(username);
        SecurityContextHolder.clearContext();
    }

}
