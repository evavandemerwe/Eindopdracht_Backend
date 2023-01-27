package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.management.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person , Long> {

    /**
     * A repository for retrieval of persons
     * @param firstName which is requested to be searched for in the database
     * @return an Optional list of Persons - Persons are returned if found
     */
    List<Person> findByLastNameContaining(String firstName);

    Person findByUserId(Long id);

}
