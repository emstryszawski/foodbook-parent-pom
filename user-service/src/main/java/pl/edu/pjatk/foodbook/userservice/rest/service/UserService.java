package pl.edu.pjatk.foodbook.userservice.rest.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.userservice.exception.NotFoundException;
import pl.edu.pjatk.foodbook.userservice.repository.UserRepository;
import pl.edu.pjatk.foodbook.userservice.repository.model.User;
import pl.edu.pjatk.foodbook.userservice.rest.dto.NewRequestUser;

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

    public void saveUser(NewRequestUser requestUser) {
        User user = mapper.map(requestUser, User.class);
        userRepository.save(user);
    }
}
