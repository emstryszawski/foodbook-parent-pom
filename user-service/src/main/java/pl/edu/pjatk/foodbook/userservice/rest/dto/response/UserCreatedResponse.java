package pl.edu.pjatk.foodbook.userservice.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedResponse {
    private UUID id;
    private String realName;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
