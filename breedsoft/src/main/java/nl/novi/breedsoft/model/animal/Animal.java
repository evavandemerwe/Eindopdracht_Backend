package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public abstract class Animal {
    private String food;
    private String color;
    private double weightInGrams;
    private Sex sex;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private Bloodtemperature bloodtemperature;
    private Birthmethod birthmethod;
    private AnimalType animalType;
    private Animal father;
    private Animal mother;
    private List<Animal> siblings;

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAge() {
        LocalDate today = LocalDate.now();
        Period periodBetweenTodayAndBirthDate = Period.between(getDateOfBirth(), today);
        return  periodBetweenTodayAndBirthDate.getYears();
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

    public Bloodtemperature getBloodtemperature() {
        return bloodtemperature;
    }

    public void setBloodtemperature(Bloodtemperature bloodtemperature) {
        this.bloodtemperature = bloodtemperature;
    }

    public Birthmethod getBirthmethod() {
        return birthmethod;
    }

    public void setBirthmethod(Birthmethod birthmethod) {
        this.birthmethod = birthmethod;
    }

    public Animal getFather() {
        return father;
    }

    public void setFather(Animal father) {
        this.father = father;
    }

    public Animal getMother() {
        return mother;
    }

    public void setMother(Animal mother) {
        this.mother = mother;
    }
}
