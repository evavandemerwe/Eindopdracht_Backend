package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;

import jakarta.persistence.*;
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Mammal extends Animal {

    private static final Birthmethod birthmethod = Birthmethod.livebirth;
    private String kindOfHair;
    private int numberOfTeeth;

    public Mammal() {
        super();
        super.setBloodTemperature(Bloodtemperature.warmblooded);
    }

    public String getKindOfHair() {
        return kindOfHair;
    }
    public void setKindOfHair(String kindOfHair) {
        this.kindOfHair = kindOfHair;
    }
    public int getNumberOfTeeth() {
        return numberOfTeeth;
    }
    public void setNumberOfTeeth(int numberOfTeeth) {
        this.numberOfTeeth = numberOfTeeth;
    }
}
