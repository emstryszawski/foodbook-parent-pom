package pl.edu.pjatk.foodbook.recipeservice.rest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.recipeservice.repository.ProductRepository;
import pl.edu.pjatk.foodbook.recipeservice.repository.RecipeRepository;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Macros;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Product;
import pl.edu.pjatk.foodbook.recipeservice.repository.model.Recipe;
import pl.edu.pjatk.foodbook.recipeservice.rest.dto.CreateRecipeInput;
import pl.edu.pjatk.foodbook.recipeservice.rest.dto.GetRecipe;
import pl.edu.pjatk.foodbook.recipeservice.rest.dto.ProductDto;
import pl.edu.pjatk.foodbook.recipeservice.rest.exception.RecipeNotFoundException;
import pl.edu.pjatk.foodbook.recipeservice.swagger.product.api.ProductControllerApi;
import pl.edu.pjatk.foodbook.recipeservice.swagger.product.model.AddProductInput;
import pl.edu.pjatk.foodbook.recipeservice.swagger.product.model.GetProduct;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final ProductRepository productRepository;

    private final ProductControllerApi productApi;

    public RecipeService(RecipeRepository recipeRepository, ProductRepository productRepository, ProductControllerApi productApi) {
        this.recipeRepository = recipeRepository;
        this.productRepository = productRepository;
        this.productApi = productApi;
    }

    public GetRecipe createRecipe(CreateRecipeInput input) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Recipe recipeToBeSaved = buildRecipeFromInput(input, username);
        Recipe recipe = recipeRepository.save(recipeToBeSaved);
        List<Product> products = getProducts(recipe, input.getProducts());
        productRepository.saveAll(products);
        recipe.setProductIds(products);
        return mapToDto(recipe);
    }

    private List<Product> getProducts(Recipe recipe, List<ProductDto> products) {
        return products.stream()
            .map(product -> {
                Product.ProductBuilder builder = Product.builder();
                if (product.getId() == null) {
                    ResponseEntity<GetProduct> response =
                        productApi.addProduct(new AddProductInput().name(product.getName()));

                    if (response != null && response.getBody() != null) {
                        GetProduct getProduct = response.getBody();
                        builder.id(getProduct.getId());
                    }
                } else {
                    builder.id(product.getId());
                }
                return builder.unit(product.getUnit())
                    .amount(product.getAmount())
                    .recipe(recipe)
                    .build();
            }).toList();
    }

    private GetRecipe mapToDto(Recipe recipe) {
        return GetRecipe.builder()
            .id(recipe.getId())
            .name(recipe.getName())
            .owner(recipe.getOwner())
            .products(
                recipe.getProductIds().stream()
                    .map(productId -> {
                        GetProduct getProduct = productApi.getProduct(productId.getId()).getBody();
                        return ProductDto.builder()
                            .id(getProduct.getId())
                            .name(getProduct.getName())
                            .amount(productId.getAmount())
                            .unit(productId.getUnit())
                            .build();
                    }).toList()
            )
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

    private Recipe buildRecipeFromInput(CreateRecipeInput input, String username) {
        Macros macros = input.getMacros();
        Recipe.RecipeBuilder recipeBuilder = Recipe.builder()
            .name(input.getName())
            .owner(username)
            .recipe(input.getRecipe())
            .mainProducts(input.getMainProducts())
            .description(input.getDescription());
        if (macros != null) {
            recipeBuilder.proteins(macros.getProteins())
                .carbs(macros.getCarbs())
                .fats(macros.getFats())
                .calories(macros.getCalories())
                .build();
        }
        return recipeBuilder.build();
    }

    public GetRecipe getRecipeById(UUID recipeId) {
        Recipe recipe = recipeRepository
            .findById(recipeId)
            .orElseThrow(RecipeNotFoundException::new);
        return mapToDto(recipe);
    }

    public List<GetRecipe> getAllRecipes() {
        return recipeRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public GetRecipe updateRecipe(UUID recipeId, CreateRecipeInput updateRecipeInput) {
        recipeRepository
            .findById(recipeId)
            .orElseThrow(RecipeNotFoundException::new);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Recipe recipe = buildRecipeFromInput(updateRecipeInput, username);
        recipe.setId(recipeId);
        List<Product> products = getProducts(recipe, updateRecipeInput.getProducts());
        productRepository.saveAll(products);
        recipe.setProductIds(products);
        recipeRepository.save(recipe);
        return mapToDto(recipe);
    }

    public void deleteRecipe(UUID recipeId) {
        recipeRepository.deleteById(recipeId);
    }

    public List<GetRecipe> getRecipeByOwner(String owner) {
        return recipeRepository.findAllByOwner(owner).stream()
            .map(this::mapToDto)
            .toList();
    }
}
