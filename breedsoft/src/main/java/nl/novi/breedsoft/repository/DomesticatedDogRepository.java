package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.management.DomesticatedDog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DomesticatedDogRepository extends JpaRepository<DomesticatedDog, Long> {
    List<DomesticatedDog> findByNameContaining(String name);
}
