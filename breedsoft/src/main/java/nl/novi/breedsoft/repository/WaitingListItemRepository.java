package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.WaitingListItem;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaitingListItemRepository extends JpaRepository<WaitingListItem, Long> {
    List<WaitingListItem> findByKindOfHairContaining(String kindOfHair);

    List<WaitingListItem> findByBreed(Breed breed);

    List<WaitingListItem> findBySex(Sex sex);

    @Query(
            value = "SELECT * FROM waiting_list_items wl WHERE wl.sex = :sex AND wl.kind_of_hair = :kindOfHair AND wl.breed = :breed",
            nativeQuery = true
    )
    List<WaitingListItem> findByCriteria(@Param("sex")String sex, @Param("kindOfHair")String kindOfHair, @Param("breed")String breed);

}