package nl.novi.breedsoft.model.animal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.novi.breedsoft.model.animal.enumerations.AnimalType;
import nl.novi.breedsoft.model.animal.enumerations.Birthmethod;
import nl.novi.breedsoft.model.animal.enumerations.Bloodtemperature;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "persons")
public class Person extends Mammal {

    //Declaration of variables
    //Create primary key with a unique value, making sure each table starts counting at 1 and is self-incremental
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String street;
    private int houseNumber;
    private String houseNumberExtension;
    private String zipCode;
    private String city;
    private String country;

    //A person can have 0 to * dogs
    @OneToMany(mappedBy = "person")
    private List<Dog> dogs;

    //constructor
    public Person() {
        super();
        super.setNumberOfTeeth(32);
        super.setBirthMethod(Birthmethod.livebirth);
        super.setBloodTemperature(Bloodtemperature.warmblooded);
        super.setAnimalType(AnimalType.omnivore);
    }

    //getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getHouseNumberExtension() {
        return houseNumberExtension;
    }

    public void setHouseNumberExtension(String houseNumberExtension) {
        this.houseNumberExtension = houseNumberExtension;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }
}
