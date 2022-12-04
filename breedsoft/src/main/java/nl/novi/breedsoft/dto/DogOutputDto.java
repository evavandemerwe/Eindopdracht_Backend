package nl.novi.breedsoft.dto;

import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;

import java.util.ArrayList;

public class DogOutputDto {
    public long id;
    public String name;
    public String chipnumber;
    public Breed breed;
    public int dogYears;

    private ArrayList<String> Litter;
    public BreedGroup breedGroup;
}
