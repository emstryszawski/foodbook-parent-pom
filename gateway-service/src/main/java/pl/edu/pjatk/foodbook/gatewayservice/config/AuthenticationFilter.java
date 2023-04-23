package pl.edu.pjatk.foodbook.gatewayservice.config;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pl.edu.pjatk.foodbook.gatewayservice.feign.clients.AuthClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;

    private final AuthClient authApi;

    public AuthenticationFilter(
        RouterValidator routerValidator,
        AuthClient authApi) {
        this.routerValidator = routerValidator;
        this.authApi = authApi;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            String accessToken = extractToken(request);

            if (accessToken == null || accessToken.isEmpty()) {
                return onError(exchange, "Authorization header/accessToken is missing");
            }

            boolean isValid = Boolean.TRUE.equals(authApi.validateToken(accessToken).getBody());

            if (isValid) {
                populateRequestWithAuthHeader(request, accessToken);
            } else {
                return onError(exchange, "Authorization header is invalid");
            }
        }
        return chain.filter(exchange);
    }

    @Nullable
    private String extractToken(@NotNull ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").stream()
            .filter(StringUtils::isNotEmpty)
            .filter(bearerToken -> bearerToken.startsWith("Bearer "))
            .map(bearerToken -> bearerToken.substring(7))
            .findFirst()
            .orElse(null);
    }

    private static void populateRequestWithAuthHeader(ServerHttpRequest request, String jwt) {
        request.mutate()
            .header("Authorization", "Bearer " + jwt)
            .build();
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.writeWith(Flux.just(buffer));
    }
}
