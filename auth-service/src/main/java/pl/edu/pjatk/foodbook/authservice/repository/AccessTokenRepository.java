package pl.edu.pjatk.foodbook.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjatk.foodbook.authservice.repository.model.AccessToken;
import pl.edu.pjatk.foodbook.authservice.repository.model.RefreshToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, Integer> {

    Optional<AccessToken> findByToken(String token);

    Optional<List<AccessToken>> findByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);

    AccessToken findByRefreshToken(RefreshToken refreshToken);
}
