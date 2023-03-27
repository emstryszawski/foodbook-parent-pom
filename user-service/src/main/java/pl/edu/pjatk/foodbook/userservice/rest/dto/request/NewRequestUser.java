package pl.edu.pjatk.foodbook.userservice.rest.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pjatk.foodbook.userservice.repository.model.Role;
import pl.edu.pjatk.foodbook.userservice.rest.dto.annotations.ValidEnum;
import pl.edu.pjatk.foodbook.userservice.rest.dto.annotations.ValidPassword;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewRequestUser {
    @Size(min = 3, max = 48, message = "Username must be of 3 - 48 characters")
    @NotNull(message = "Username must not be null")
    @NotEmpty(message = "Username must not be empty")
    private String username;
    @NotNull(message = "Password must not be null")
    @NotEmpty(message = "Password must not be empty")
    @ValidPassword
    private String password;
    @Size(min = 4, max = 96, message = "Real name must be of 4 - 96 characters")
    @Nullable
    private String realName;
    @Email
    @NotNull(message = "Email must not be null")
    @NotEmpty(message = "Email must not be empty")
    private String email;
    @NotNull(message = "Role must not be null")
    @NotEmpty(message = "Role must not be empty")
    @ValidEnum(value = Role.class)
    private String role;
}
