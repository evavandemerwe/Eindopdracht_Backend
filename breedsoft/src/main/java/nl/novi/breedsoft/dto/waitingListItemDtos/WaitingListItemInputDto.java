package nl.novi.breedsoft.dto.waitingListItemDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import lombok.Data;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.utility.ValueOfEnum;

@Data
public class WaitingListItemInputDto {
    @Null
    private Long id;

    @Null
    private int numberOnList;

    @JsonIncludeProperties({"id", "firstName", "lastName", "street", "houseNumber", "houseNumberExtension", "zipCode", "city", "country"})
    private Person person;

    @NotEmpty(message = "Please enter a preferred hair type.")
    private String KindOfHair;

    @NotEmpty(message = "Please enter a breed.")
    @ValueOfEnum(enumClass = Breed.class, message = "Invalid breed.")
    private String breed;

    @NotEmpty(message = "Please enter a sex")
    @ValueOfEnum(enumClass = Sex.class, message = "Invalid sex")
    private String sex;

}