package pl.edu.pjatk.foodbook.authservice.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.pjatk.foodbook.authservice.repository.AuthenticationRepository;
import pl.edu.pjatk.foodbook.authservice.repository.RoleRepository;
import pl.edu.pjatk.foodbook.authservice.repository.model.Role;
import pl.edu.pjatk.foodbook.authservice.repository.model.RoleEnum;

import java.security.Key;
import java.util.List;

@Configuration
public class AppConfig {

    private final String PUBLIC_KEY;

    private final AuthenticationRepository authRepo;

    public AppConfig(@Value("${foodbook.security.jwt.key}") String key, AuthenticationRepository authRepo) {
        this.PUBLIC_KEY = key;
        this.authRepo = authRepo;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> authRepo.findById(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with given username: { " + username + " }"
            ));
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

    @Bean
    public Key getSignInKey() {
        byte[] keyBytes = PUBLIC_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Bean
    public CommandLineRunner insertRoles(RoleRepository roleRepository) {
        return args -> roleRepository.saveAll(List.of(
            new Role(RoleEnum.USER),
            new Role(RoleEnum.MODERATOR),
            new Role(RoleEnum.ADMIN)
        ));
    }
}
