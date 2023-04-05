package pl.edu.pjatk.foodbook.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authFilter;

    public GatewayConfig(AuthenticationFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service",
                route -> route
                    .path("/auth/**")
                    .filters(filter -> filter.filter(authFilter)
                        .rewritePath("/auth/(?<path>.*)", "/$\\{path}"))
                    .uri("lb://auth-service"))
            .build();
    }
}
