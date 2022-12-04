package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import java.util.ArrayList;

import javax.persistence.*;

@Entity
@Table(name="dogs")
public class Dog {

    //Create primary key with a unique value, making sure each table starts counting at 1 and is self-incremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //Declaration of variables
    private String name;
    private Long chipnumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "breed")
    private Breed breed;
    private ArrayList<String> Litter;
    int dogYears;
    @Enumerated(EnumType.STRING)
    @Column(name = "breed_group")
    private BreedGroup breedGroup;

    //Getters and setter
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