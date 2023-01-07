package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.management.WaitingListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingListItemRepository extends JpaRepository<WaitingListItem, Long> {

}