package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.animal.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person , Long> {
    Person findByLastNameContaining(String firstName);

}
