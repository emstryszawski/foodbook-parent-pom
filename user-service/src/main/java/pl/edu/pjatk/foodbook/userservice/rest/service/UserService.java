package pl.edu.pjatk.foodbook.userservice.rest.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.userservice.exception.UserAlreadyExistsException;
import pl.edu.pjatk.foodbook.userservice.exception.UserNotFoundException;
import pl.edu.pjatk.foodbook.userservice.repository.UserRepository;
import pl.edu.pjatk.foodbook.userservice.repository.model.User;
import pl.edu.pjatk.foodbook.userservice.rest.dto.request.CreateUserInput;
import pl.edu.pjatk.foodbook.userservice.rest.dto.response.UserRepresentation;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public UserRepresentation getUser(String username) {
        User user = userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("User was not found with given username: " + username));
        return mapper.map(user, UserRepresentation.class);
    }

    public UserRepresentation saveUser(CreateUserInput requestUser) {
        User user = mapper.map(requestUser, User.class);
        String email = user.getEmail();
        String username = user.getUsername();

        validateIfAlreadyExists(email, username);

        User savedUser = userRepository.save(user);

        return mapper.map(savedUser, UserRepresentation.class);
    }

    private void validateIfAlreadyExists(String email, String username) throws UserAlreadyExistsException {
        if (isEmailTaken(email)) {
            throw new UserAlreadyExistsException("An account with email: " + email + " already exists.", "email");
        }
        if (isUsernameTaken(username)) {
            throw new UserAlreadyExistsException("An account with username: " + username + " already exists.", "username");
        }
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
