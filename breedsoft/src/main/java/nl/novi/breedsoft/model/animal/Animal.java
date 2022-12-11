package nl.novi.breedsoft.model.animal;
import nl.novi.breedsoft.model.animal.enumerations.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Animal {

    //Create primary key with a unique value, making sure each table starts counting at 1 and is self-incremental
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String food;
    private String color;
    private double weightInGrams;
    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private Sex sex;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    @Enumerated(EnumType.STRING)
    @Column(name = "bloodtemperature")
    private Bloodtemperature bloodtemperature;
    @Enumerated(EnumType.STRING)
    @Column(name = "birthmethod")
    private Birthmethod birthmethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "animalType")
    private AnimalType animalType;

    //private Animal father;

    //private Animal mother;
    //private List<Animal> siblings;

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
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

 /*   public Animal getFather() {
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
    }*/
}
