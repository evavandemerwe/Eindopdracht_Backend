package nl.novi.breedsoft.dto;
import lombok.Data;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.utility.ValueOfEnum;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;

@Data
public class DogInputDto {
    @NotEmpty(message = "Please enter the name of the dog")
    private String name;

    @NotEmpty(message = "Please enter the hair color of the dog")
    private String color;

    @NotEmpty(message = "Please enter dog's food")
    private String food;

    @NotEmpty(message = "Please enter a sex")
    @ValueOfEnum(enumClass = Sex.class, message = "Invalid sex")
    private String sex;

    @NotNull(message = "Please enter a weight")
    private double weightInGrams;

    @NotEmpty(message = "Please enter kind of hair")
    private String kindOfHair;

    private int dogYears;

    @NotNull
    private LocalDate dateOfBirth;

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

    private ArrayList<String> litter;
}
