package pl.edu.pjatk.foodbook.userservice.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRepresentation {
    private String username;
    private String email;
    private String realName;
}
