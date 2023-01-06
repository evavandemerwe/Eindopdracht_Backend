package nl.novi.breedsoft.model.management;

import jakarta.persistence.*;
import nl.novi.breedsoft.model.animal.enumerations.*;
import nl.novi.breedsoft.model.management.enumerations.Breed;

@Entity
@Table(name = "waiting_list_item")
public class WaitingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private int numberOnList;
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private Breed breed;
    private String KindOfHair;
    private Sex sex;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
