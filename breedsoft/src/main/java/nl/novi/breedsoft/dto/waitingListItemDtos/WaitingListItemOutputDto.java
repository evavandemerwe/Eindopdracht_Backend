package nl.novi.breedsoft.dto.waitingListItemDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.enumerations.Breed;


@Data
public class WaitingListItemOutputDto {
    private Long id;
    private int numberOnList;
    @JsonIncludeProperties({"id", "firstName", "lastName", "street", "houseNumber", "houseNumberExtension", "zipCode", "city", "country"})
    private Person person;
    private String KindOfHair;
    private Breed breed;
    private Sex sex;

}

