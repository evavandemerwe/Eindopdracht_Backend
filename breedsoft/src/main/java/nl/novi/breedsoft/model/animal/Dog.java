package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import java.util.ArrayList;

import javax.persistence.*;

@Entity
@Table(name = "dogs")
public class Dog extends Mammal{

    //Declaration of variables

    //Create primary key with a unique value, making sure each table starts counting at 1 and is self-incremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String chipNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "breed")
    private Breed breed;
    private ArrayList<String> Litter;
    int dogYears;
    @Enumerated(EnumType.STRING)
    @Column(name = "breed_group")
    private BreedGroup breedGroup;

    //Constructor

    public Dog() {
        super.setNumberOfTeeth(42);
        super.setBirthMethod(Birthmethod.livebirth);
        super.setBloodTemperature(Bloodtemperature.warmblooded);
        super.setAnimalType(AnimalType.carnivore);
    }

    //Getters and setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getChipnumber() {
        return chipNumber;
    }

    public void setChipnumber(String chipnumber) {
        this.chipNumber = chipnumber;
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
        if(super.getDateOfBirth() == null) {
            return 0;
        }else {
            dogYears = (super.getAge() * 7);
            return dogYears;
        }
    }

    public void setDogYears(int dogYears) {
        if(super.getDateOfBirth() == null) {
            this.dogYears = 0;
        }
        else {
            dogYears = (super.getAge() * 7);
            this.dogYears = dogYears;
        }
    }

    public String getChipNumber() {
        return chipNumber;
    }

    public void setChipNumber(String chipNumber) {
        this.chipNumber = chipNumber;
    }
}