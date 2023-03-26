package pl.edu.pjatk.foodbook.authservice.rest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationRequest;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationResponse;
import pl.edu.pjatk.foodbook.authservice.rest.dto.RegisterRequest;
import pl.edu.pjatk.foodbook.authservice.swagger.user.api.UserControllerApiClient;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.NewRequestUser;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    //    TODO
    private final UserControllerApiClient userApiClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        var user = new NewRequestUser()
                       .firstname(request.getFirstname())
                       .lastname(request.getLastname())
                       .email(request.getEmail())
                       .username(request.getUsername())
                       .password(passwordEncoder.encode(request.getPassword()))
                       .role(NewRequestUser.RoleEnum.USER);

        HttpStatusCode statusCode = userApiClient.saveUser(user).getStatusCode();
        if (statusCode != HttpStatus.CREATED) {
            throw new Exception("can't create user");
        }

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                   .token(jwtToken)
                   .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        var user = Optional
                       .ofNullable(
                           userApiClient.getUserByUsername(request.getUsername())
                       )
                       .orElseThrow(); // TODO throw correct exception and then catch it
        String jwtToken = jwtService.generateToken(new NewRequestUser().username(user.getBody().getUsername())); // TODO TMP

        return AuthenticationResponse.builder()
                   .token(jwtToken)
                   .build();
    }
}
