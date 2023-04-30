package pl.edu.pjatk.foodbook.recipeservice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Macros;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Product;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecipeInput {
    private String name;
    private String recipe;
    private List<Product> products;
    private Macros macros;
    private String mainProducts;
    private String description;
}

