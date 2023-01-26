package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import jakarta.persistence.*;

//Designates a class whose mapping information is applied to the entities that inherit from it.
//A mapped superclass has no separate table defined for it.
//Not used as an entity.
@MappedSuperclass
public abstract class Mammal extends Animal {
    //All mammals give live birth
    private static final Birthmethod BIRTHMETHOD = Birthmethod.livebirth;
    //All mammals are warmblooded
    private static final Bloodtemperature BLOODTEMPERATURE = Bloodtemperature.warmblooded;
    private String kindOfHair;
    private int numberOfTeeth;

    public Mammal() {
        super();
    }

    public String getKindOfHair() {
        return kindOfHair;
    }
    public void setKindOfHair(String kindOfHair) {
        this.kindOfHair = kindOfHair;
    }
    public void setNumberOfTeeth(int numberOfTeeth) {
        this.numberOfTeeth = numberOfTeeth;
    }
}
