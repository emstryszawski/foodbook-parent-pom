package pl.edu.pjatk.foodbook.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.edu.pjatk.foodbook.authservice.security.GatewayFilter;
import pl.edu.pjatk.foodbook.authservice.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String AUTH_RESOURCE_PATH = "/api/v1/auth/**";
    public static final String API_DOCS_PATH = "/v3/api-docs";
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final GatewayFilter gatewayFilter;
    private final AuthenticationProvider authProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .addFilterBefore(gatewayFilter, BasicAuthenticationFilter.class)
            .authorizeHttpRequests()
            .requestMatchers(API_DOCS_PATH, API_DOCS_PATH + ".yaml",
                "/api/v1/auth/register",
                "/api/v1/auth/authenticate",
                "/api/v1/auth/validate-token")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .logout()
            .logoutUrl(AUTH_RESOURCE_PATH + "/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }
}
