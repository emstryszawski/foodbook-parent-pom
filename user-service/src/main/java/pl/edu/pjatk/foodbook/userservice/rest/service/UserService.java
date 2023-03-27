package pl.edu.pjatk.foodbook.userservice.rest.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.userservice.exception.AlreadyExistsException;
import pl.edu.pjatk.foodbook.userservice.exception.NotFoundException;
import pl.edu.pjatk.foodbook.userservice.repository.UserRepository;
import pl.edu.pjatk.foodbook.userservice.repository.model.User;
import pl.edu.pjatk.foodbook.userservice.rest.dto.request.NewRequestUser;
import pl.edu.pjatk.foodbook.userservice.rest.dto.response.UserCreatedResponse;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public User getUser(String username) {
        return userRepository
                   .findByUsername(username)
                   .orElseThrow(() -> new NotFoundException("User was not found with given username: " + username));
    }

    public UserCreatedResponse saveUser(NewRequestUser requestUser) throws AlreadyExistsException {
        User user = mapper.map(requestUser, User.class);
        String email = user.getEmail();
        String username = user.getUsername();

        validateIfAlreadyExists(email, username);

        User savedUser = userRepository.save(user);

        UserCreatedResponse response = mapper.map(savedUser, UserCreatedResponse.class);
        response.setCreatedAt(LocalDateTime.now());
        return response;
    }

    private void validateIfAlreadyExists(String email, String username) throws AlreadyExistsException {
        if (isEmailTaken(email)) {
            throw new AlreadyExistsException("An account with email: " + email + " already exists.", "email");
        }
        if (isUsernameTaken(username)) {
            throw new AlreadyExistsException("An account with username: " + username + " already exists.", "username");
        }
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean isUsernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
