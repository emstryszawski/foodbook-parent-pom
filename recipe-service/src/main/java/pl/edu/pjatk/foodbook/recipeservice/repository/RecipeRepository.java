package pl.edu.pjatk.foodbook.recipeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Recipe;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, UUID> {

    @Transactional
    List<Recipe> findAllByOwner(String owner);
}
