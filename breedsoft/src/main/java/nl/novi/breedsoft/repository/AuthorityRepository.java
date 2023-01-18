package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.authority.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    /**
     * A repository for retrieval of authorities
     * @param authority which is requested to be searched for in the database
     * @return an Optional Authority - Authority is returned if found
     */
    Optional<Authority> findByAuthority(String authority);
}
