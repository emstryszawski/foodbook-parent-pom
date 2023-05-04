package pl.edu.pjatk.foodbook.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authFilter;

    public GatewayConfig(@Lazy AuthenticationFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth-service",
                route -> route
                    .path("/auth/api/v1/**")
                    .filters(filter -> filter
                        .filter(authFilter)
                        .rewritePath("/auth/(?<path>.*)", "/$\\{path}")
                        .addRequestHeader("X-Gateway", "localhost:8080"))
                    .uri("lb://auth-service"))
            .route("foodbook-service",
                route -> route
                    .path("/foodbook/api/v1/**")
                    .filters(filter -> filter
                        .filter(authFilter)
                        .rewritePath("/foodbook/(?<path>.*)", "/$\\{path}")
                        .addRequestHeader("X-Gateway", "localhost:8080"))
                    .uri("lb://foodbook-service"))
            .route("recipe-service",
                route -> route
                    .path("/recipe/api/v1/**")
                    .filters(filter -> filter
                        .filter(authFilter)
                        .rewritePath("/recipe/(?<path>.*)", "/$\\{path}")
                        .addRequestHeader("X-Gateway", "localhost:8080"))
                    .uri("lb://recipe-service"))
            .route("product-service",
                route -> route
                    .path("/product/api/v1/**")
                    .filters(filter -> filter
                        .filter(authFilter)
                        .rewritePath("/product/(?<path>.*)", "/$\\{path}")
                        .addRequestHeader("X-Gateway", "localhost:8080"))
                    .uri("lb://product-service"))

            .build();
    }
}
