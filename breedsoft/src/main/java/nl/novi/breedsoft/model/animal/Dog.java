package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class Dog extends Mammal{

    //Declaration of variables
    int dogYears;

    //Constructor
    public Dog() {
        super();
        super.setNumberOfTeeth(42);
        super.setAnimalType(AnimalType.carnivore);
    }

    //Getters and setter
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
}