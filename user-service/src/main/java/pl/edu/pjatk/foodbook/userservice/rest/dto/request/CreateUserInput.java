package pl.edu.pjatk.foodbook.userservice.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(accessMode = Schema.AccessMode.READ_WRITE)
public class CreateUserInput {
    @NotNull
    private String username;
    @Nullable
    private String realName;
    @NotNull
    private String email;

    public CreateUserInput() {
    }

    public CreateUserInput(String username, String realName, String email) {
        this.username = username;
        this.realName = realName;
        this.email = email;
    }
}
