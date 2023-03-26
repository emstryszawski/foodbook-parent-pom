package pl.edu.pjatk.foodbook.userservice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pjatk.foodbook.userservice.repository.model.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewRequestUser {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
}
