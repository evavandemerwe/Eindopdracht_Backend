package nl.novi.breedsoft.dto;

import lombok.Data;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;

import java.util.ArrayList;

@Data
public class DogOutputDto {
    private long id;
    private String name;
    private String chipnumber;
    private Breed breed;
    private int dogYears;
    private ArrayList<String> Litter;
    private BreedGroup breedGroup;
}
