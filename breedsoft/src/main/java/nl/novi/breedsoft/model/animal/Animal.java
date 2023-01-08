package nl.novi.breedsoft.model.animal;
import nl.novi.breedsoft.model.animal.enumerations.*;
import jakarta.persistence.*;
import nl.novi.breedsoft.model.animal.interfaces.Hearable;
import nl.novi.breedsoft.model.animal.interfaces.Seeable;

import java.time.LocalDate;
import java.time.Period;

//Designates a class whose mapping information is applied to the entities that inherit from it.
//A mapped superclass has no separate table defined for it.
//Not used as an entity.
@MappedSuperclass
public abstract class Animal implements Hearable, Seeable {
    private String food;
    private String hairColor;
    private double weightInGrams;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_temperature")
    private Bloodtemperature bloodTemperature;
    @Enumerated(EnumType.STRING)
    @Column(name = "birth_method")
    private Birthmethod birthMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "animal_type")
    private AnimalType animalType;

    //Getters and Setters
    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public int getAge() {
        if(getDateOfBirth() == null) {
            return 0;
        }else {
            LocalDate today = LocalDate.now();
            Period periodBetweenTodayAndBirthDate = Period.between(getDateOfBirth(), today);
            return periodBetweenTodayAndBirthDate.getYears();
        }
    }

    public double getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(double weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(LocalDate dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public void setBloodTemperature(Bloodtemperature bloodTemperature) {
        this.bloodTemperature = bloodTemperature;
    }

    public void setBirthMethod(Birthmethod birthMethod) {
        this.birthMethod = birthMethod;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

}
