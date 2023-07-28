package pl.edu.pjatk.foodbook.recipeservice.repository.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ProductRecipeKey.class)
public class Product {
    @Id
    private UUID productId;
    @Id
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipeId;
    private Integer amount;
    private String unit;
}
