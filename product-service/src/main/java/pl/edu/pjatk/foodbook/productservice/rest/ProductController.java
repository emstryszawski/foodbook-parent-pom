package pl.edu.pjatk.foodbook.productservice.rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pjatk.foodbook.productservice.rest.dto.AddProductInput;
import pl.edu.pjatk.foodbook.productservice.rest.dto.GetProduct;
import pl.edu.pjatk.foodbook.productservice.rest.service.ProductService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<GetProduct> addProduct(@Valid @RequestBody AddProductInput input) {
        log.info("Request to add new product {}", input);
        GetProduct product = productService.addProduct(input);
        log.info("Added new product {}", product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetProduct> getProduct(@PathVariable("productId") UUID productId) {
        log.info("Request to find product with id {}", productId);
        GetProduct product = productService.getProductById(productId);
        log.info("Product with id {} was found {}", productId, product);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<GetProduct>> getAllProducts() {
        log.info("Request to get all product");
        List<GetProduct> allProducts = productService.getAllProducts();
        log.info("Received list of all products {}", allProducts);
        return ResponseEntity.ok(allProducts);
    }
}
