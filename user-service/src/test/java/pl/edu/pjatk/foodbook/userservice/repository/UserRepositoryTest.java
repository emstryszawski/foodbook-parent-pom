package pl.edu.pjatk.foodbook.userservice.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.edu.pjatk.foodbook.userservice.TestMocks;
import pl.edu.pjatk.foodbook.userservice.repository.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldFindUserByEmail() {
        // given
        User user = entityManager.merge(TestMocks.getMock(User.class));

        // when
        Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());

        // then
        assertAll(() -> {
            assertTrue(userByEmail.isPresent());
            assertEquals(user, userByEmail.get());
        });

    }

    @Test
    public void shouldFindUserByUsername() {
        // given
        User user = entityManager.merge(TestMocks.getMock(User.class));

        // when
        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());

        // then
        assertAll(() -> {
            assertTrue(userByUsername.isPresent());
            assertEquals(user, userByUsername.get());
        });
    }
}