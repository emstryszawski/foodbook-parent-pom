package pl.edu.pjatk.foodbook.recipeservice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Macros;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Product;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetRecipe {
    private UUID id;
    private String name;
    private String owner;
    private String recipe;
    private List<Product> products;
    private Macros macros;
    private String mainProducts;
    private String description;
}
