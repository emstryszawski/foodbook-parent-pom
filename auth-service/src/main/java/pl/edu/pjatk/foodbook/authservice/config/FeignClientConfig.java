package pl.edu.pjatk.foodbook.authservice.config;

import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.pjatk.foodbook.authservice.rest.feign.UserServiceClientErrorDecoder;

@Configuration
public class FeignClientConfig {

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
        @Value("${feign.clients.user-service.username}") String username,
        @Value("${feign.clients.user-service.password}") String password
    ) {
        return new BasicAuthRequestInterceptor(username, password);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserServiceClientErrorDecoder();
    }
}
