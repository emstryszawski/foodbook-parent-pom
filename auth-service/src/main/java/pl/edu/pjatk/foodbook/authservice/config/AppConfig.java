package pl.edu.pjatk.foodbook.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.pjatk.foodbook.authservice.swagger.user.api.UserControllerApiClient;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.User;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final UserControllerApiClient userApiClient;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> Optional.ofNullable(userApiClient.getUserByUsername(username))
                               .map(ResponseEntity::getBody)
                               .map(AppConfig::mapToUserDetails)
                               .orElseThrow(() -> new UsernameNotFoundException(
                                   "User not found with given username: { " + username + " }"
                               ));
    }

    private static UserDetails mapToUserDetails(User user) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getAuthorities().stream()
                           .map(pl.edu.pjatk.foodbook.authservice.swagger.user.model.GrantedAuthority::getAuthority)
                           .map(SimpleGrantedAuthority::new)
                           .collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return user.getAccountNonExpired();
            }

            @Override
            public boolean isAccountNonLocked() {
                return user.getAccountNonLocked();
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return user.getCredentialsNonExpired();
            }

            @Override
            public boolean isEnabled() {
                return user.getEnabled();
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
