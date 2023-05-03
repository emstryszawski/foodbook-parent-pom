package pl.edu.pjatk.foodbook.authservice.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    private final AuthInterceptor authInterceptor;

    public FeignClientConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return authInterceptor;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new UserServiceClientErrorDecoder();
    }
}
