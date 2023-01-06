package nl.novi.breedsoft.model.animal;

import nl.novi.breedsoft.model.animal.enumerations.*;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Dog extends Mammal{

    //Declaration of variables
    //Create primary key with a unique value, making sure that when someone want to insert a non-domesticated dog
    //each table starts counting at 1 and is self-incremental
    @Id
    @GeneratedValue
    private Long id;
    @Column(name="parent_id")
    private Long parentId;
    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name="parent_id")
    private List<Dog> Litter;

    int dogYears;

    //Constructor
    public Dog() {
        super();
        super.setNumberOfTeeth(42);
        super.setBirthMethod(Birthmethod.livebirth);
        super.setBloodTemperature(Bloodtemperature.warmblooded);
        super.setAnimalType(AnimalType.carnivore);
    }

    //Getters and setter
    public Long getId() {
        return id;
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
}