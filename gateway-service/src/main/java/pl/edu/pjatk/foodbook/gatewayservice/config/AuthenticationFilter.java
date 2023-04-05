package pl.edu.pjatk.foodbook.gatewayservice.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pl.edu.pjatk.foodbook.gatewayservice.service.RouterValidator;
import pl.edu.pjatk.foodbook.gatewayservice.service.TokenService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final RouterValidator routerValidator;
    private final TokenService tokenService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isSecured.test(request)) {
            String token = tokenService.extractToken(request);

            if (token == null) {
                return onError(exchange, "Authorization header/token is missing");
            }

            if (tokenService.isValid(token)) {
                this.populateRequestWithHeaders(request, token);
            } else {
                return onError(exchange, "Authorization header is invalid");
            }
        }
        return chain.filter(exchange);
    }

    private void populateRequestWithHeaders(ServerHttpRequest request, String token) {
        Claims claims = tokenService.extractClaims(token);
        request.mutate()
            .header("id", claims.getSubject())
            .header("role", claims.get("roles", String.class)) // TODO ?
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
