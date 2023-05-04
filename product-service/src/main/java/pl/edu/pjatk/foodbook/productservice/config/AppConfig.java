package pl.edu.pjatk.foodbook.productservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.pjatk.foodbook.productservice.repository.AllergenRepository;
import pl.edu.pjatk.foodbook.productservice.repository.CategoryRepository;
import pl.edu.pjatk.foodbook.productservice.repository.model.Allergen;
import pl.edu.pjatk.foodbook.productservice.repository.model.Category;

import java.util.List;

@Configuration
public class AppConfig {

    private final CategoryRepository categoryRepository;

    private final AllergenRepository allergenRepository;

    public AppConfig(CategoryRepository categoryRepository, AllergenRepository allergenRepository) {
        this.categoryRepository = categoryRepository;
        this.allergenRepository = allergenRepository;
    }

    @Bean
    public CommandLineRunner insertData() {
        return args -> {
            categoryRepository.saveAll(
                List.of(
                    new Category("Fruits"),
                    new Category("Vegetables"),
                    new Category("Grains"),
                    new Category("Dairy products"),
                    new Category("Meat and poultry"),
                    new Category("Seafood"),
                    new Category("Legumes"),
                    new Category("Nuts and seeds"),
                    new Category("Oils and fats"),
                    new Category("Sweets and desserts"),
                    new Category("Beverages"),
                    new Category("Snacks"),
                    new Category("Condiments and spices"),
                    new Category("Prepared and processed foods"),
                    new Category("Ethnic and regional cuisines")
                ));
            allergenRepository.saveAll(
                List.of(
                    new Allergen("Milk"),
                    new Allergen("Eggs"),
                    new Allergen("Fish"),
                    new Allergen("Crustacean shellfish"),
                    new Allergen("Tree nuts"),
                    new Allergen("Peanuts"),
                    new Allergen("Wheat"),
                    new Allergen("Soybeans"),
                    new Allergen("Sesame seeds"),
                    new Allergen("Mustard"),
                    new Allergen("Sulfites")
                    )
            );
        };
    }
}
