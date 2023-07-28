package pl.edu.pjatk.foodbook.recipeservice.rest.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @Nullable
    private UUID id;
    @NotBlank
    private String name;
    @NotNull
    @Size(min = 1)
    private int amount;
    @NotBlank
    private String unit;
    @NotNull
    private int proteins;
    @NotNull
    private int fats;
    @NotNull
    private int carbs;
    @NotNull
    private int calories;
}
