package pl.edu.pjatk.foodbook.authservice.rest.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationRequest;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationResponse;
import pl.edu.pjatk.foodbook.authservice.rest.dto.RegisterRequest;
import pl.edu.pjatk.foodbook.authservice.swagger.user.api.UserControllerApiClient;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.NewRequestUser;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.User;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.UserCreatedResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserControllerApiClient userApiClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    private final ModelMapper mapper = new ModelMapper();

    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        var newRequestUser = new NewRequestUser()
                                 .realName(request.getFirstname() + " " + request.getLastname())
                                 .email(request.getEmail())
                                 .username(request.getUsername())
                                 .password(passwordEncoder.encode(request.getPassword()))
                                 .role(String.valueOf(User.RoleEnum.USER));

        ResponseEntity<UserCreatedResponse> response = userApiClient.saveUser(newRequestUser);

        // TODO move error from userApiClient to client response
        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new Exception("can't create newRequestUser");
        }

        var createdUser = Optional
                              .ofNullable(response.getBody())
                              .orElseThrow(); // TODO throw normal error about newRequestUser couldn't be fetch or no data for that newRequestUser

        User user = mapper.map(createdUser, User.class);

        var jwtToken = jwtService.generateToken(user);
        // TODO save token
        jwtService.saveUserToken(user.getId(), jwtToken);

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

        var response = Optional
                           .ofNullable(
                               userApiClient.getUserByUsername(request.getUsername())
                           )
                           .orElseThrow(); // TODO throw correct exception and then catch it

        var user = Optional
                       .ofNullable(response.getBody())
                       .orElseThrow(); // TODO

        String jwtToken = jwtService.generateToken(user);
        jwtService.revokeUserTokens(user.getId());
        jwtService.saveUserToken(user.getId(), jwtToken);

        return AuthenticationResponse.builder()
                   .token(jwtToken)
                   .build();
    }
}
