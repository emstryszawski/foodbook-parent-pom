package pl.edu.pjatk.foodbook.productservice.rest.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pjatk.foodbook.productservice.repository.ProductRepository;
import pl.edu.pjatk.foodbook.productservice.repository.model.Product;
import pl.edu.pjatk.foodbook.productservice.rest.dto.AddProductInput;
import pl.edu.pjatk.foodbook.productservice.rest.dto.GetProduct;
import pl.edu.pjatk.foodbook.productservice.rest.exception.ProductNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public GetProduct addProduct(AddProductInput input) {
        Product product = buildProductFromInput(input);
        Product savedProduct = productRepository.save(product);
        return mapToDto(savedProduct);
    }

    public GetProduct getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
        return mapToDto(product);
    }

    private GetProduct mapToDto(Product savedProduct) {
        return GetProduct.builder()
            .id(savedProduct.getId())
            .name(savedProduct.getName())
            .addedBy(savedProduct.getAddedBy())
            .brand(savedProduct.getBrand())
            .category(savedProduct.getCategory())
            .price(savedProduct.getPrice())
            .proteins(savedProduct.getProteins())
            .fats(savedProduct.getFats())
            .carbs(savedProduct.getCarbs())
            .calories(savedProduct.getCalories())
            .description(savedProduct.getDescription())
            .allergens(savedProduct.getAllergens())
            .build();
    }

    private static Product buildProductFromInput(AddProductInput input) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return Product.builder()
            .name(input.getName())
            .addedBy(username)
            .brand(input.getBrand())
            .category(input.getCategory())
            .price(input.getPrice())
            .proteins(input.getProteins())
            .fats(input.getFats())
            .carbs(input.getCarbs())
            .calories(input.getCalories())
            .description(input.getDescription())
            .allergens(input.getAllergens())
            .build();
    }

    public List<GetProduct> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::mapToDto)
            .toList();
    }
}
