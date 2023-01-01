package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import java.util.List;

import jakarta.persistence.*;

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

    @Column(name="parent_id")
    private Long parentId;
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name="parent_id")
    private List<Dog> Litter;

    int dogYears;
    @Enumerated(EnumType.STRING)
    @Column(name = "breed_group")
    private BreedGroup breedGroup;

    // boolean is a primitive type so cannot be null or nullable, default value has to be set
    @Column(columnDefinition = "boolean default true")
    private boolean canHear = true;

    // boolean is a primitive type so cannot be null or nullable, default value has to be set
    @Column(columnDefinition = "boolean default true")
    private boolean canSee = true;
    //A dog has one owner
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private byte[] dogImage;

    //Constructor
    public Dog() {
        super.setNumberOfTeeth(42);
        super.setBirthMethod(Birthmethod.livebirth);
        super.setBloodTemperature(Bloodtemperature.warmblooded);
        super.setAnimalType(AnimalType.carnivore);
    }

    //Getters and setter

    public Long getId() {
        return (id != null) ? id : 0;
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

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<Dog> getLitter() {
        return Litter;
    }

    public void setLitter(List<Dog> litter) {
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
    public Person getPerson() {
        return this.person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean canHear() {
        return canHear;
    }

    @Override
    public boolean canSee() {
        return canSee;
    }

    public byte[] getDogImage() {
        return dogImage;
    }

    public void setDogImage(byte[] dogImage) {
        this.dogImage = dogImage;
    }
}