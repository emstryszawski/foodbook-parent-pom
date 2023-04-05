package pl.edu.pjatk.foodbook.gatewayservice.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouterValidator {

    private static final List<String> unsecured = List.of(
        "auth/api/v1/auth/register",
        "auth/api/v1/auth/authenticate"
    );

    public Predicate<ServerHttpRequest> isSecured =
        request -> unsecured
            .stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
