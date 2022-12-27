package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.authority.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Optional<Authority> findByAuthority(String authority);
}
