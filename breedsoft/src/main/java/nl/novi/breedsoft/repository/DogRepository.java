package nl.novi.breedsoft.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import nl.novi.breedsoft.model.animal.Dog;

public interface DogRepository extends JpaRepository<Dog, Long> {
    public Iterable<Dog> findByNameContaining(String name);
}
