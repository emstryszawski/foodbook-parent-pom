package pl.edu.pjatk.foodbook.authservice.rest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.authservice.repository.AuthenticationRepository;
import pl.edu.pjatk.foodbook.authservice.repository.model.Authentication;
import pl.edu.pjatk.foodbook.authservice.repository.model.Role;
import pl.edu.pjatk.foodbook.authservice.repository.model.RoleEnum;
import pl.edu.pjatk.foodbook.authservice.rest.dto.AuthenticationRequest;
import pl.edu.pjatk.foodbook.authservice.rest.dto.RegisterRequest;
import pl.edu.pjatk.foodbook.authservice.swagger.user.api.UserApi;
import pl.edu.pjatk.foodbook.authservice.swagger.user.model.CreateUserInput;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserApi userApi;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    private final AuthenticationRepository authRepository;

    public void register(RegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        Authentication authentication = new Authentication(
            username, encodedPassword, Set.of(new Role(RoleEnum.USER))
        );
        authRepository.save(authentication);

        String firstname = request.getFirstname();
        CreateUserInput createUserInput = new CreateUserInput()
            .realName(firstname +
                (firstname != null && !firstname.isEmpty() ? " " : "") +
                request.getLastname())
            .email(request.getEmail())
            .username(username);

        userApi.saveUser(createUserInput);
    }

    public void authenticate(AuthenticationRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new pl.edu.pjatk.foodbook.authservice.rest.exception.AuthenticationException(
                "Authentication failed, invalid credentials");
        }
    }
}
