package nl.novi.breedsoft.dto;

import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;

import java.util.ArrayList;

public class DogOutputDto {
    private long id;
    private String name;
    private String chipnumber;
    private Breed breed;
    private int dogYears;
    private ArrayList<String> Litter;
    private BreedGroup breedGroup;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public int getDogYears() {
        return dogYears;
    }

    public void setDogYears(int dogYears) {
        this.dogYears = dogYears;
    }

    public ArrayList<String> getLitter() {
        return Litter;
    }

    public void setLitter(ArrayList<String> litter) {
        Litter = litter;
    }

    public BreedGroup getBreedGroup() {
        return breedGroup;
    }

    public void setBreedGroup(BreedGroup breedGroup) {
        this.breedGroup = breedGroup;
    }
}
