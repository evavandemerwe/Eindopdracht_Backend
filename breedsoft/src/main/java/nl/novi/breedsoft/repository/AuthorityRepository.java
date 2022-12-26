package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.authority.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
