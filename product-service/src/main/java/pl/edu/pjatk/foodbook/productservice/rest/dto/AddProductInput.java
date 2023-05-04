package pl.edu.pjatk.foodbook.productservice.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pjatk.foodbook.productservice.repository.model.Allergen;
import pl.edu.pjatk.foodbook.productservice.repository.model.Category;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductInput {
    private String name;
    private String description;
    private String brand;
    private Category category;
    private BigDecimal price;
    private Integer proteins;
    private Integer fats;
    private Integer carbs;
    private Integer calories;
    private List<Allergen> allergens;
}
