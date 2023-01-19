package nl.novi.breedsoft.repository;

import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.WaitingListItem;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface WaitingListItemRepository extends JpaRepository<WaitingListItem, Long> {

    /**
     * A repository for retrieval of waiting list items by kind of hair
     * @param kindOfHair which is requested to be searched for in the database
     * @return a list with waiting list items based on requested kind of hair
     */
    List<WaitingListItem> findByKindOfHairContaining(String kindOfHair);

    /**
     * A repository for retrieval of waiting list items by breed
     * @param breed which is requested to be searched for in the database
     * @return a list with waiting list items based on requested breed
     */
    List<WaitingListItem> findByBreed(Breed breed);

    /**
     * A repository for retrieval of waiting list items by sex
     * @param sex which is requested to be searched for in the database
     * @return a list with waiting list items based on requested sex
     */
    List<WaitingListItem> findBySex(Sex sex);

    /**
     * A repository for retrieval of waiting list items by kind of hair, sex and breed
     * @param kindOfHair which is requested to be searched for in the database
     * @param sex which is requested to be searched for in the database
     * @param breed which is requested to be searched for in the database
     * @return a list with waiting list items based on requested kind of hair, sex and breed
     */
    @Query(
            value = "SELECT * FROM waiting_list_items wl WHERE wl.sex = :sex AND wl.kind_of_hair = :kindOfHair AND wl.breed = :breed",
            nativeQuery = true
    )
    List<WaitingListItem> findByCriteria(@Param("sex")String sex, @Param("kindOfHair")String kindOfHair, @Param("breed")String breed);
}