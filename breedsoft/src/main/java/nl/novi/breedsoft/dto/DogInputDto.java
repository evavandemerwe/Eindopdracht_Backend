package nl.novi.breedsoft.dto;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;

public class DogInputDto {
    @NotBlank
    public String name;
    @NotBlank
    @Size(min=15, max=15)
    //A chipnumber is always exactly 15 numbers long
    public long chipnumber;

    @NotBlank
    @Enumerated(EnumType.ORDINAL)
    public Breed breed;

    @NotBlank
    @Size(min=0, max=20)
    public int dogYears;

    private ArrayList<String> Litter;

    @NotBlank
    @Enumerated(EnumType.ORDINAL)
    public BreedGroup breedGroup;
}
