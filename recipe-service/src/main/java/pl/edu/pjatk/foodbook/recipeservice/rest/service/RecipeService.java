package pl.edu.pjatk.foodbook.recipeservice.rest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.recipeservice.repository.RecipeRepository;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Macros;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Recipe;
import pl.edu.pjatk.foodbook.recipeservice.rest.dto.CreateRecipeInput;
import pl.edu.pjatk.foodbook.recipeservice.rest.dto.GetRecipe;
import pl.edu.pjatk.foodbook.recipeservice.rest.exception.RecipeNotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecipeService {

    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public GetRecipe createRecipe(CreateRecipeInput input) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Recipe recipeToBeSaved = buildRecipeFromInput(input, username);
        Recipe recipe = repository.save(recipeToBeSaved);
        return mapToDto(recipe);
    }

    private static GetRecipe mapToDto(Recipe recipe) {
        return GetRecipe.builder()
            .id(recipe.getId())
            .name(recipe.getName())
            .owner(recipe.getOwner())
            //.products() TODO this should be external api call
            .recipe(recipe.getRecipe())
            .macros(Macros.builder()
                .proteins(recipe.getProteins())
                .carbs(recipe.getCarbs())
                .fats(recipe.getFats())
                .calories(recipe.getCalories())
                .build())
            .mainProducts(recipe.getMainProducts())
            .description(recipe.getDescription())
            .build();
    }

    private static Recipe buildRecipeFromInput(CreateRecipeInput input, String username) {
        Macros macros = input.getMacros();
        Recipe.RecipeBuilder recipeBuilder = Recipe.builder()
            .name(input.getName())
            .owner(username)
            .recipe(input.getRecipe())
            // TODO products
            .mainProducts(input.getMainProducts())
            .description(input.getDescription());
        Recipe recipe;
        if (macros != null) {
            recipe = recipeBuilder.proteins(macros.getProteins())
                .carbs(macros.getCarbs())
                .fats(macros.getFats())
                .calories(macros.getCalories())
                .build();
        } else {
            recipe = recipeBuilder.build();
        }
        return recipe;
    }

    public GetRecipe getRecipeById(UUID recipeId) {
        Recipe recipe = repository
            .findById(recipeId)
            .orElseThrow(RecipeNotFoundException::new);
        return mapToDto(recipe);
    }

    public List<GetRecipe> getAllRecipes() {
        return repository.findAll().stream()
            .map(RecipeService::mapToDto)
            .collect(Collectors.toList());
    }

    public GetRecipe updateRecipe(UUID recipeId, CreateRecipeInput updateRecipeInput) {
        repository
            .findById(recipeId)
            .orElseThrow(RecipeNotFoundException::new);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Recipe recipe = buildRecipeFromInput(updateRecipeInput, username);
        recipe.setId(recipeId);
        repository.save(recipe);
        return mapToDto(recipe);
    }

    public void deleteRecipe(UUID recipeId) {
        repository.deleteById(recipeId);
    }

    public List<GetRecipe> getRecipeByOwner(String owner) {
        return repository.findAllByOwner(owner).stream()
            .map(RecipeService::mapToDto)
            .toList();
    }
}
