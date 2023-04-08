package pl.edu.pjatk.foodbook.authservice.rest.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationRequest;
import pl.edu.pjatk.foodbook.authservice.rest.dto.RegisterRequest;
import pl.edu.pjatk.foodbook.authservice.swagger.user.api.UserControllerApiClient;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.NewRequestUser;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserControllerApiClient userApiClient;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    private final ModelMapper mapper = new ModelMapper();

    public User register(RegisterRequest request) {
        var newRequestUser = new NewRequestUser()
            .realName(request.getFirstname() + " " + request.getLastname())
            .email(request.getEmail())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(String.valueOf(User.RoleEnum.USER));

        return mapper.map(userApiClient.saveUser(newRequestUser).getBody(), User.class);
    }

    public User authenticate(AuthenticationRequest request) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        // TODO should return some dto
        return userApiClient.getUserByUsername(request.getUsername()).getBody();
    }
}
