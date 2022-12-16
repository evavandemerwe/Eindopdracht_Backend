package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.animal.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person , Long> {
    List<Person> findByLastNameContaining(String firstName);

}
