package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.authority.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
        attributePaths = {"authorities"})
    Optional<User> findByUsername(String username);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"authorities"})
    List<User> findAll();
}
