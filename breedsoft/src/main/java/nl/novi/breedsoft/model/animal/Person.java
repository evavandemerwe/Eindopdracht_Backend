package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.AnimalType;
import nl.novi.breedsoft.model.animal.enumerations.Birthmethod;
import nl.novi.breedsoft.model.animal.enumerations.Bloodtemperature;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "persons")
public class Person extends Human{

private String firstName;
private String lastName;
private String street;
private int houseNumber;
private String houseNumberExtention;
private String zipCode;
private String city;
private String country;

//constructor

    public Person() {
        super();
        super.setNumberOfTeeth(32);
        super.setBirthmethod(Birthmethod.livebirth);
        super.setBloodtemperature(Bloodtemperature.warmblooded);
        super.setAnimalType(AnimalType.omnivore);
    }

//getters and setters
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

    public String getHouseNumberExtention() {
        return houseNumberExtention;
    }

    public void setHouseNumberExtention(String houseNumberExtention) {
        this.houseNumberExtention = houseNumberExtention;
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
}
