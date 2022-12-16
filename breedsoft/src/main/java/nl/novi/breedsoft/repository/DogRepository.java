package nl.novi.breedsoft.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import nl.novi.breedsoft.model.animal.Dog;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {
    List<Dog> findByNameContaining(String name);
}
