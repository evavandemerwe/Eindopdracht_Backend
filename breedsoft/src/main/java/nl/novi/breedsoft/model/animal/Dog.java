package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dogs")
public class Dog {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private long chipnumber;
    private Breed breed;
    private ArrayList<String> Litter;
    int dogYears;

    private BreedGroup breedGroup;

    public Dog() {
        super();
    }

    public long getId(){
        return id;
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