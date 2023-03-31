package pl.edu.pjatk.foodbook.userservice.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewRequestUser {
    private String username;
    private String password;
    private String realName;
    private String email;
    private String role;
}
