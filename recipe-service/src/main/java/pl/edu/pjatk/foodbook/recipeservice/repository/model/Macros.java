package pl.edu.pjatk.foodbook.recipeservice.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Macros {
    private Integer proteins;
    private Integer carbs;
    private Integer fats;
    private Integer calories;
}
