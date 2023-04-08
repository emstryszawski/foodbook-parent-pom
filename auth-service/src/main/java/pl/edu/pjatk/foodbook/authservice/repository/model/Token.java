package pl.edu.pjatk.foodbook.authservice.repository.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class Token {
    private boolean expired;
    private boolean revoked;
    private LocalDateTime expiredAt;
    private LocalDateTime validUntil;

    public abstract boolean isValid();

    public boolean isExpired() {
        expired = expiredAt != null || !ZonedDateTime.now(ZoneId.systemDefault()).isBefore(ZonedDateTime.of(validUntil, ZoneId.systemDefault()));
        if (expired) {
            expiredAt = expiredAt == null ? validUntil : expiredAt;
        }
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
        expiredAt = LocalDateTime.now();
    }
}
