package pl.edu.pjatk.foodbook.authservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationResponse;
import pl.edu.pjatk.foodbook.authservice.rest.service.JwtTokenService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements RequestInterceptor {

    private final JwtTokenService jwtTokenService;

    @Override
    public void apply(RequestTemplate template) {
        String username;
        try {
            username = new ObjectMapper().readValue(new String(template.body()), CreateUserInput.class).getUsername();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        AuthenticationResponse authenticationResponse = jwtTokenService.generateTokens(username);
        String jwt = authenticationResponse.getToken();
        template.header("Authorization", "Bearer " + jwt);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CreateUserInput {
        private String username;
        private String realName;
        private String email;
    }

}
