package pl.edu.pjatk.foodbook.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pjatk.foodbook.authservice.repository.model.Authentication;

public interface AuthenticationRepository extends JpaRepository<Authentication, String> {

}
