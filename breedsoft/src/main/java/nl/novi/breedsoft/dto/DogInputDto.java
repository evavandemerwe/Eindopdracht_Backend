package nl.novi.breedsoft.dto;
import javax.validation.constraints.*;

import lombok.Data;
import nl.novi.breedsoft.model.animal.Person;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.utility.ValueOfEnum;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class DogInputDto {
    @NotEmpty(message = "Please enter the name of the dog")
    private String name;

    @NotEmpty(message = "Please enter the hair color of the dog")
    private String hairColor;

    @NotEmpty(message = "Please enter dog's food")
    private String food;

    @NotEmpty(message = "Please enter a sex")
    @ValueOfEnum(enumClass = Sex.class, message = "Invalid sex")
    private String sex;

    @NotNull(message = "Please enter a weight")
    private double weightInGrams;

    @NotEmpty(message = "Please enter kind of hair")
    private String kindOfHair;

    @NotNull
    private LocalDate dateOfBirth;

    //Can be empty
    private LocalDate dateOfDeath;

    @NotEmpty(message = "Please enter a chip number.")
    @Pattern(regexp = "[0-9]{15}")
    private String chipNumber;

    @NotEmpty(message = "Please enter a breed.")
    @ValueOfEnum(enumClass = Breed.class, message = "Invalid breed")
    private String breed;

    @NotEmpty(message = "Please enter a breed group.")
    @ValueOfEnum(enumClass = BreedGroup.class, message = "Invalid breed group")
    private String breedGroup;

    //Can be empty
    private ArrayList<String> litter;

    private int dogYears;

    private Person person;
}
