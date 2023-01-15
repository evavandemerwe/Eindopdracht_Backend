package nl.novi.breedsoft.dto.waitingListItemDtos;

import lombok.Data;
import nl.novi.breedsoft.model.management.Person;

@Data
public class WaitingListItemPatchDto {
    private Long id;
    private int numberOnList;
    private Person person;
    private String KindOfHair;
    private String breed;
    private String sex;
}
