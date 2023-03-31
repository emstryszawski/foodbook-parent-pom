package pl.edu.pjatk.foodbook.authservice.rest.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pjatk.foodbook.authservice.rest.dto.annotation.ValidPassword;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Size(min = 3, max = 48, message = "Username must be of 3 - 48 characters")
    @NotNull(message = "Username must not be null")
    @NotEmpty(message = "Username must not be empty")
    private String username;
    @NotNull(message = "Password must not be null")
    @NotEmpty(message = "Password must not be empty")
    @ValidPassword
    private String password;
    @Size(min = 2, max = 48, message = "Firstname must be of 2 - 48 characters")
    @Nullable
    private String firstname;
    @Size(min = 2, max = 48, message = "Lastname must be of 2 - 48 characters")
    @Nullable
    private String lastname;
    @Email
    @NotNull(message = "Email must not be null")
    @NotEmpty(message = "Email must not be empty")
    private String email;
}
