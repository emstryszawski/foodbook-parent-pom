package pl.edu.pjatk.foodbook.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjatk.foodbook.productservice.repository.model.Allergen;

@Repository
public interface AllergenRepository extends JpaRepository<Allergen, String> {

}
