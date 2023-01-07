package nl.novi.breedsoft.model.management;

import jakarta.persistence.*;
import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.enumerations.Status;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.BreedGroup;

import java.util.List;

@Entity
@Table(name = "domesticated_dogs")
public class DomesticatedDog extends Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String chipNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "breed")
    private Breed breed;

    @Enumerated(EnumType.STRING)
    @Column(name = "breed_group")
    private BreedGroup breedGroup;

    // boolean is a primitive type so cannot be null or nullable, default value has to be set
    @Column(columnDefinition = "boolean default true")
    private boolean canHear = true;

    @Column(columnDefinition = "boolean default true")
    private boolean canSee = true;

    //A domesticated dog has one owner
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private byte[] dogImage;

    @Enumerated(EnumType.STRING)
    @Column(name = "dog_status")
    Status dogStatus;

    @Column(name="parent_id")
    private Long parentId;

    @OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.MERGE)
    @JoinColumn(name="parent_id")
    private List<DomesticatedDog> litter;

    @OneToMany(mappedBy = "domesticatedDog", cascade=CascadeType.MERGE)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "domesticatedDog", cascade = CascadeType.MERGE)
    private List<MedicalData> medicalData;

    public DomesticatedDog() {
        super();
    }

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
    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<DomesticatedDog> getLitter() {
        return this.litter;
    }
    public void setLitter(List<DomesticatedDog> litter) {
        litter = litter;
    }

    public Status getDogStatus() {
        return dogStatus;
    }

    public void setDogStatus(Status dogStatus) {
        this.dogStatus = dogStatus;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<MedicalData> getMedicalData() {
        return medicalData;
    }

    public void setMedicalData(List<MedicalData> medicalData) {
        this.medicalData = medicalData;
    }
}
