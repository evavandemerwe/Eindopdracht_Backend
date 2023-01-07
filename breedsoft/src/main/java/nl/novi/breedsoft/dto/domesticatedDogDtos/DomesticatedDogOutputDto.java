package nl.novi.breedsoft.dto.domesticatedDogDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.model.management.Appointment;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.BreedGroup;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.animal.enumerations.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class DomesticatedDogOutputDto {
    private long id;
    private String name;
    private String hairColor;
    private String food;
    private Sex sex;
    private double weightInGrams;
    private String kindOfHair;
    private int dogYears;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private String chipNumber;
    private Breed breed;
    private BreedGroup breedGroup;
    @JsonIncludeProperties({"id", "name", "sex", "dateOfBirth", "breed", "kindOfHair", "haircolor"})
    private List<DomesticatedDog> litters;

    private boolean canSee;
    private boolean canHear;
    private Long parentId;
    private Status dogStatus;

    @JsonIncludeProperties({"id", "appointmentDateTime", "subject"})
    private List<Appointment> appointments;

    @JsonIncludeProperties("id")
    private List<MedicalData> medicalData;

    @JsonIncludeProperties({"id", "firstName", "lastName", "street", "houseNumber", "houseNumberExtension", "zipCode", "city", "country"})
    private Person person;

    private byte[] dogImage;

    //Because of recursion when getting a list of domesticated dogs from entity DomesticatedDog,
    //we limit the litter to fields that are of interest for a dog owner
    public void setLitters(List<DomesticatedDog> litters) {
        if(litters != null) {
            this.litters = new ArrayList<>();
            for (DomesticatedDog dog : litters) {
                DomesticatedDog newDog = new DomesticatedDog();
                newDog.setId(dog.getId());
                newDog.setName(dog.getName());
                newDog.setKindOfHair(dog.getKindOfHair());
                newDog.setBreed(dog.getBreed());
                newDog.setSex(dog.getSex());
                newDog.setDateOfBirth(dog.getDateOfBirth());
                this.litters.add(newDog);
            }
        } else {
            this.litters = null;
        }
    }
}
