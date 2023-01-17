package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.management.DomesticatedDog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DomesticatedDogRepository extends JpaRepository<DomesticatedDog, Long> {
    /**
     * A repository for retrieval of domesticated dogs
     * @param name which is requested to be searched for in the database
     * @return an Optional DomesticatedDog - DomesticatedDog is returned if found
     */
    List<DomesticatedDog> findByNameContaining(String name);
}
