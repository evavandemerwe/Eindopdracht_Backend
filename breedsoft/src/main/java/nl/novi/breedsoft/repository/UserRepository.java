package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.authority.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * A repository for retrieval of users by username
     * @param username which is requested to be searched for in the database
     * @return an Optional User - User is returned if found
     */
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
        attributePaths = {"authorities"})
    Optional<User> findByUsername(String username);

}
