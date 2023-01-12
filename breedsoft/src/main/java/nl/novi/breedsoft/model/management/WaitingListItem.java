package nl.novi.breedsoft.model.management;

import jakarta.persistence.*;
import nl.novi.breedsoft.model.animal.enumerations.*;
import nl.novi.breedsoft.model.management.enumerations.Breed;

@Entity
@Table(name = "waiting_list_items")
public class WaitingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int numberOnList;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
    private String KindOfHair;
    @Enumerated(EnumType.STRING)
    @Column(name = "breed")
    private Breed breed;
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;

    //Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public int getNumberOnList() {
        return numberOnList;
    }

    public void setNumberOnList(int numberOnList) {
        this.numberOnList = numberOnList;
    }

    public String getKindOfHair() {
        return KindOfHair;
    }

    public void setKindOfHair(String kindOfHair) {
        KindOfHair = kindOfHair;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
