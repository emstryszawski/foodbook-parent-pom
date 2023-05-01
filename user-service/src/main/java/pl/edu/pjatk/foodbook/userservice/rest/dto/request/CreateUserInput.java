package pl.edu.pjatk.foodbook.userservice.rest.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserInput {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @Nullable
    private String realName;
    @NotNull
    private String email;
    @NotNull
    private String role;
}
