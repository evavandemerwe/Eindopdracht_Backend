package nl.novi.breedsoft.dto;

import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;
import nl.novi.breedsoft.utility.ValueOfEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.util.ArrayList;

public class DogInputDto {
    @NotEmpty(message = "Please enter the name of the dog")
    public String name;
    @NotEmpty(message = "Please enter a chipnumber.")
    @Pattern(regexp = "[0-9]{15}")
    //A chipnumber is always exactly 15 numbers long
    public String chipnumber;
    @NotEmpty(message = "Please enter a breed")
    @ValueOfEnum(enumClass = Breed.class, message = "Invalid breed")
    @Enumerated(EnumType.STRING)
    public String breed;

    @NotNull(message = "Please enter the age of the dog.")
    @Min(0)
    @Max(22)
    public int dogYears;
    public ArrayList<String> litter;

    @NotEmpty(message = "Please enter a breed group.")
    @ValueOfEnum(enumClass = BreedGroup.class, message = "Invalid breed group")
    @Enumerated(EnumType.STRING)
    public String breedGroup;
}
