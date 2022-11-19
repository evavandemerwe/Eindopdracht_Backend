package nl.novi.breedsoft.repository;
import org.springframework.data.repository.CrudRepository;
import nl.novi.breedsoft.model.animal.Dog;

public interface DogRepository extends CrudRepository<Dog, Long>{
    public Iterable<Dog> findByNameContaining(String name);
}
