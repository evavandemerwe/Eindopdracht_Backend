package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import java.util.ArrayList;

import javax.persistence.*;

@Entity
@Table(name = "dogs")
public class Dog extends Mammal{

    //Declaration of variables
    private String name;
    private String chipnumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "breed")
    private Breed breed;
    private ArrayList<String> Litter;
    int dogYears;
    @Enumerated(EnumType.STRING)
    @Column(name = "breed_group")
    private BreedGroup breedGroup;

    //Getters and setter
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