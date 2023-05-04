package pl.edu.pjatk.foodbook.recipeservice.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private UUID id;
    private Integer amount;
    private String unit;
    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
