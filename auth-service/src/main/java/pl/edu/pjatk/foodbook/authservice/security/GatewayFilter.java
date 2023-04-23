package pl.edu.pjatk.foodbook.authservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayFilter extends OncePerRequestFilter {

    private final String gatewayIp;

    private final String gatewayPort;

    public GatewayFilter(
        @Value("${foodbook.security.gateway.ip}") String gatewayIp,
        @Value("${foodbook.security.gateway.port}") String gatewayPort) {
        this.gatewayIp = gatewayIp;
        this.gatewayPort = gatewayPort;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String gatewayHeader = request.getHeader("X-Gateway");

        if (gatewayHeader == null || gatewayHeader.isEmpty()) {
            response.setStatus(401);
            response.getWriter().write("Unauthorized access: Request must come from gateway");
            return;
        } else if (!gatewayHeader.equals(gatewayIp + ":" + gatewayPort)) {
            response.setStatus(403);
            response.getWriter().write("Forbidden access: Gateway authentication failed");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
