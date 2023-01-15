package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.WaitingListItem;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitingListItemRepository extends JpaRepository<WaitingListItem, Long> {
    List<WaitingListItem> findByKindOfHairContaining(String kindOfHair);

}