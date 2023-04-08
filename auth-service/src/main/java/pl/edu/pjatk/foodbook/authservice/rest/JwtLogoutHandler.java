package pl.edu.pjatk.foodbook.authservice.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import pl.edu.pjatk.foodbook.authservice.rest.service.JwtTokenService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        if (isAuthHeaderInvalid(authHeader)) {
            try {
                response.sendError(401, "Unauthorized");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        String jwtToken = getJwtToken(authHeader);
        jwtService.removeAll(jwtToken);
        SecurityContextHolder.clearContext();
    }

    private static String getJwtToken(String authHeader) {
        return authHeader.substring(7);
    }

    private static boolean isAuthHeaderInvalid(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }
}
