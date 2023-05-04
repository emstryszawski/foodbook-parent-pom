package pl.edu.pjatk.foodbook.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pjatk.foodbook.productservice.repository.model.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
