package pl.edu.pjatk.foodbook.recipeservice.repository.model;

import java.io.Serializable;
import java.util.UUID;

public class ProductRecipeKey implements Serializable {
    private UUID productId;
    private UUID recipeId;
}
