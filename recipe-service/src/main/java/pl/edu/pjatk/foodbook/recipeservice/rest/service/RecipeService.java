package pl.edu.pjatk.foodbook.recipeservice.rest.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
import java.util.function.Function;
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
                        productApi.addProduct(getInput(product));

                    if (response != null && response.getBody() != null) {
                        GetProduct getProduct = response.getBody();
                        builder.productId(getProduct.getId());
                    }
                } else {
                    builder.productId(product.getId());
                }
                return builder
                    .amount(product.getAmount())
                    .unit(product.getUnit())
                    .recipeId(recipe)
                    .build();
            }).toList();
    }

    private static AddProductInput getInput(ProductDto product) {
        String unit = product.getUnit();
        int amount = product.getAmount();
        int proteins = product.getProteins();
        int carbs = product.getCarbs();
        int fats = product.getFats();
        int calories = product.getCalories();

        Function<Integer, Integer> func = macro -> macro;

        switch (unit) {
            case "litre", "kilogram" -> func = macro -> macro != 0 ? macro / (amount * 10) : 0;
            case "gram", "millilitre" -> func = macro -> macro != 0 ? macro / (amount / 100) : 0;
        }

        proteins = func.apply(proteins);
        carbs = func.apply(carbs);
        fats = func.apply(fats);
        calories = func.apply(calories);

        return new AddProductInput()
            .name(product.getName())
            .proteins(proteins)
            .carbs(carbs)
            .fats(fats)
            .calories(calories);
    }

    private GetRecipe mapToDto(Recipe recipe) {
        GetRecipe getRecipe = GetRecipe.builder()
            .id(recipe.getId())
            .name(recipe.getName())
            .owner(recipe.getOwner())
            .products(
                fetchProductsFromProductApi(recipe)
            )
            .recipe(recipe.getRecipe())
            .mainProducts(recipe.getMainProducts())
            .description(recipe.getDescription())
            .build();

        Macros macros = getRecipe.countMacros();
        getRecipe.setMacros(macros);

        return getRecipe;
    }

    @NotNull
    private List<ProductDto> fetchProductsFromProductApi(Recipe recipe) {
        return recipe.getProductIds().stream()
            .map(productId -> {
                GetProduct getProduct = productApi.getProduct(productId.getProductId()).getBody();
                return ProductDto.builder()
                    .id(getProduct.getId())
                    .name(getProduct.getName())
                    .amount(productId.getAmount())
                    .unit(productId.getUnit())
                    .proteins(getProduct.getProteins())
                    .fats(getProduct.getFats())
                    .carbs(getProduct.getCarbs())
                    .calories(getProduct.getCalories())
                    .build();
            }).toList();
    }

    private Recipe buildRecipeFromInput(CreateRecipeInput input, String username) {
        Recipe.RecipeBuilder recipeBuilder = Recipe.builder()
            .name(input.getName())
            .owner(username)
            .recipe(input.getRecipe())
            .mainProducts(input.getMainProducts())
            .description(input.getDescription());
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
