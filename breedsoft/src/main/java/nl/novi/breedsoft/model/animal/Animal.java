package nl.novi.breedsoft.model.animal;
import nl.novi.breedsoft.model.animal.enumerations.*;
import jakarta.persistence.*;
import nl.novi.breedsoft.model.animal.interfaces.Hearable;
import nl.novi.breedsoft.model.animal.interfaces.Seeable;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Animal implements Hearable, Seeable {

    //Create primary key with a unique value, making sure each table starts counting at 1 and is self-incremental
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String food;
    private String hairColor;
    private double weightInGrams;
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_temperature")
    private Bloodtemperature bloodTemperature;
    @Enumerated(EnumType.STRING)
    @Column(name = "birth_method")
    private Birthmethod birthMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "animalType")
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

    public Bloodtemperature getBloodTemperature() {
        return bloodTemperature;
    }

    public void setBloodTemperature(Bloodtemperature bloodTemperature) {
        this.bloodTemperature = bloodTemperature;
    }

    public Birthmethod getBirthMethod() {
        return birthMethod;
    }

    public void setBirthMethod(Birthmethod birthMethod) {
        this.birthMethod = birthMethod;
    }

    public AnimalType getAnimalType() {
        return animalType;
    }

    public void setAnimalType(AnimalType animalType) {
        this.animalType = animalType;
    }

}
