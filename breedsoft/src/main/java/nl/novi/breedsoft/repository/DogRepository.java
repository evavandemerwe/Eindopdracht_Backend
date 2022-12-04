package nl.novi.breedsoft.repository;
import nl.novi.breedsoft.dto.DogOutputDto;
import org.springframework.data.jpa.repository.JpaRepository;
import nl.novi.breedsoft.model.animal.Dog;

public interface DogRepository extends JpaRepository<Dog, Long> {
    public Dog findByNameContaining(String name);
}
