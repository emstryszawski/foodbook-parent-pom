package pl.edu.pjatk.foodbook.authservice.repository.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken extends Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID token;

    @Override
    public boolean isValid() {
        return token != null && !isExpired() && !isRevoked();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RefreshToken that = (RefreshToken) o;
        return token != null && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
