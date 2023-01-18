package nl.novi.breedsoft.model.management;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nl.novi.breedsoft.model.animal.Mammal;
import nl.novi.breedsoft.model.animal.enumerations.AnimalType;
import nl.novi.breedsoft.model.animal.enumerations.Birthmethod;
import nl.novi.breedsoft.model.animal.enumerations.Bloodtemperature;

import jakarta.persistence.*;
import nl.novi.breedsoft.model.authority.User;

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

    // boolean is a primitive type so cannot be null or nullable, default value has to be set
    @Column(columnDefinition = "boolean default true")
    private boolean canHear = true;

    // boolean is a primitive type so cannot be null or nullable, default value has to be set
    @Column(columnDefinition = "boolean default true")
    private boolean canSee = true;

    //A person can have 0 to * dogs
    @OneToMany(mappedBy = "person", cascade=CascadeType.MERGE)
    private List<DomesticatedDog> dogs;

    @OneToMany(mappedBy = "person", cascade = CascadeType.MERGE)
    private List<WaitingListItem> waitingListItems;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    //constructor
    public Person( ) {
        super();
        super.setNumberOfTeeth(32);
        super.setBirthMethod(Birthmethod.livebirth);
        super.setBloodTemperature(Bloodtemperature.warmblooded);
        super.setAnimalType(AnimalType.omnivore);
    }


    //getters and setters
    public Long getId() {
        return (id != null) ? id : 0;
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

    public List<DomesticatedDog> getDogs() {
        return dogs;
    }

    public void setDogs(List<DomesticatedDog> dogs) {
        this.dogs = dogs;
    }

    @Override
    public boolean canHear() {
        return canHear;
    }

    @Override
    public boolean canSee() {
        return canSee;
    }

    public List<WaitingListItem> getWaitingListItems() {
        return waitingListItems;
    }
    public void setWaitingListItems(List<WaitingListItem> waitingListItems) {
        this.waitingListItems = waitingListItems;
    }
}
