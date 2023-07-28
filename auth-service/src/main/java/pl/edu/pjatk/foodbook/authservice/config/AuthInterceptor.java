package pl.edu.pjatk.foodbook.authservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String jwt = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        template.header("Authorization", "Bearer " + jwt);
    }

}
