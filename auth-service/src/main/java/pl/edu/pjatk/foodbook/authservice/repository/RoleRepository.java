package pl.edu.pjatk.foodbook.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pjatk.foodbook.authservice.repository.model.Role;
import pl.edu.pjatk.foodbook.authservice.repository.model.RoleEnum;

public interface RoleRepository extends JpaRepository<Role, RoleEnum> {

}
