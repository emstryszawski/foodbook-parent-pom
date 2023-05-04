package pl.edu.pjatk.foodbook.recipeservice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Macros;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeInput {
    // todo valid
    private String name;
    private String recipe;
    private List<ProductDto> products;
    private Macros macros;
    private String mainProducts;
    private String description;
}

