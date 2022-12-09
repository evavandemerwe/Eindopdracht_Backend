package nl.novi.breedsoft.dto;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;
import nl.novi.breedsoft.utility.ValueOfEnum;
import javax.validation.constraints.*;
import java.util.ArrayList;

public class DogInputDto {
    @NotEmpty(message = "Please enter the name of the dog")
    private String name;

    //A chipnumber is always exactly 15 numbers long
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChipnumber() {
        return chipnumber;
    }

    public void setChipnumber(String chipnumber) {
        this.chipnumber = chipnumber;
    }

    public Breed getBreed() {
        return Breed.valueOf(breed);
    }

    public void setBreed(Breed breed) {
        this.breed = breed.toString();
    }

    public int getDogYears() {
        return dogYears;
    }

    public void setDogYears(int dogYears) {
        this.dogYears = dogYears;
    }

    public ArrayList<String> getLitter() {
        return litter;
    }

    public void setLitter(ArrayList<String> litter) {
        this.litter = litter;
    }

    public BreedGroup getBreedGroup() {
        return BreedGroup.valueOf(this.breedGroup);
    }

    public void setBreedGroup(BreedGroup breedGroup) {
        this.breedGroup = breedGroup.toString();
    }
}
