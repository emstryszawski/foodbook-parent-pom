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
@Table(name = "token")
public class AccessToken extends Token {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String token;

    @OneToOne
    @JoinColumn(name = "refresh_token", referencedColumnName = "token")
    private RefreshToken refreshToken;

    private UUID userId;

    @Override
    public boolean isValid() {
        return token != null && !isExpired() && !isRevoked();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccessToken accessToken = (AccessToken) o;
        return id != null && Objects.equals(id, accessToken.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
