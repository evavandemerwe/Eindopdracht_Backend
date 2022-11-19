package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import java.util.ArrayList;

public class Dog extends Mammal {
    private String name;
    private long chipnumber;
    private Breed breed;
    private ArrayList<String> Litter;
    int dogYears;

    private BreedGroup breedGroup;

    public Dog() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BreedGroup getBreedGroup() {
        return breedGroup;
    }

    public void setBreedGroup(BreedGroup breedGroup) {
        this.breedGroup = breedGroup;
    }

    public long getChipnumber() {
        return chipnumber;
    }

    public void setChipnumber(long chipnumber) {
        this.chipnumber = chipnumber;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public ArrayList<String> getLitter() {
        return Litter;
    }

    public void setLitter(ArrayList<String> litter) {
        Litter = litter;
    }

    public int getDogYears() {
        //dogYears = (getAge() * 7);
        return dogYears;
    }

    public void setDogYears(int dogYears) {
        this.dogYears = dogYears;
    }
}