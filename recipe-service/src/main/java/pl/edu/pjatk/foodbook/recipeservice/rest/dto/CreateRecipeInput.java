package pl.edu.pjatk.foodbook.recipeservice.rest.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeInput {
    @NotBlank
    private String name;
    @NotBlank
    private String recipe;
    @NotEmpty
    private List<ProductDto> products;
    @Nullable
    private String mainProducts;
    @Nullable
    private String description;
}

