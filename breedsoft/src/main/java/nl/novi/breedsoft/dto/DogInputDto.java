package nl.novi.breedsoft.dto;
import lombok.Data;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;
import nl.novi.breedsoft.utility.ValueOfEnum;
import javax.validation.constraints.*;
import java.util.ArrayList;

@Data
public class DogInputDto {
    @NotEmpty(message = "Please enter the name of the dog")
    private String name;

    @NotEmpty(message = "Please enter a chipnumber.")
    @Pattern(regexp = "[0-9]{15}")
    private String chipnumber;

    @NotEmpty(message = "Please enter a breed.")
    @ValueOfEnum(enumClass = Breed.class, message = "Invalid breed")
    private String breed;

    @NotNull(message = "Please enter the age of the dog.")
    @Min(0)
    @Max(22)
    private int dogYears;
    private ArrayList<String> litter;

    @NotEmpty(message = "Please enter a breed group.")
    @ValueOfEnum(enumClass = BreedGroup.class, message = "Invalid breed group")
    private String breedGroup;
}
