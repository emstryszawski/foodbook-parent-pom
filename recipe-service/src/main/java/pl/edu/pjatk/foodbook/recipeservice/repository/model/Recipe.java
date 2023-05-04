package pl.edu.pjatk.foodbook.recipeservice.repository.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String owner;
    private String name;
    @Lob
    private String recipe;
    @OneToMany(mappedBy = "recipe")
    private List<Product> productIds;
    private Integer proteins;
    private Integer carbs;
    private Integer fats;
    private Integer calories;
    private String mainProducts;
    @Lob
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Recipe recipe = (Recipe) o;
        return id != null && Objects.equals(id, recipe.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
