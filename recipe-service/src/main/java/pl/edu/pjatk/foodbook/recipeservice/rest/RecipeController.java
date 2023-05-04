package pl.edu.pjatk.foodbook.recipeservice.rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.foodbook.recipeservice.rest.dto.CreateRecipeInput;
import pl.edu.pjatk.foodbook.recipeservice.rest.dto.GetRecipe;
import pl.edu.pjatk.foodbook.recipeservice.rest.service.RecipeService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<GetRecipe> createRecipe(@Valid @RequestBody CreateRecipeInput recipeInput) {
        log.info("Request to create recipe with input {}", recipeInput);
        GetRecipe createdRecipe = recipeService.createRecipe(recipeInput);
        log.info("Recipe successfully created {}", createdRecipe);
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GetRecipe>> getAllRecipes() {
        log.info("Request to get all recipes");
        List<GetRecipe> recipes = recipeService.getAllRecipes();
        log.info("Received list of all recipes {}", recipes);
        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<GetRecipe> getRecipe(@PathVariable("recipeId") UUID recipeId) {
        log.info("Request to get recipe with id {}", recipeId);
        GetRecipe recipe = recipeService.getRecipeById(recipeId);
        log.info("Recipe with id {} was found {}", recipeId, recipe);
        return ResponseEntity.ok(recipe);
    }

    @GetMapping("/owner/{username}")
    public ResponseEntity<List<GetRecipe>> getOwnerRecipes(@PathVariable("username") String username) {
        log.info("Request to get owner {} recipes", username);
        List<GetRecipe> recipes = recipeService.getRecipeByOwner(username);
        log.info("Received list of owner {} recipes {}", username, recipes);
        return ResponseEntity.ok(recipes);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<GetRecipe> updateRecipe(
        @PathVariable("recipeId") UUID recipeId,
        @Valid @RequestBody CreateRecipeInput updateRecipeInput) {
        log.info("Request to update recipe with id {} with new data {}", recipeId, updateRecipeInput);
        GetRecipe updatedRecipe = recipeService.updateRecipe(recipeId, updateRecipeInput);
        log.info("Recipe updated successfully {}", updatedRecipe);
        return ResponseEntity.ok(updatedRecipe);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteRecipeById(@PathVariable("recipeId") UUID recipeId) {
        log.info("Request to delete recipe with id {}", recipeId);
        recipeService.deleteRecipe(recipeId);
        log.info("Recipe with id {} deleted successfully", recipeId);
        return ResponseEntity.ok().build();
    }
}
