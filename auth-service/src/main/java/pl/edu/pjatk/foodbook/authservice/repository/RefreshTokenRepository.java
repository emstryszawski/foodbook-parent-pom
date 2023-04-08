package pl.edu.pjatk.foodbook.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pjatk.foodbook.authservice.repository.model.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

}
