package nl.novi.breedsoft.dto.domesticatedDogDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.Person;

import java.time.LocalDate;
import java.util.List;

@Data
public class DomesticatedDogPatchDto {
    private long id;
    private String name;
    private String hairColor;
    private String food;
    private String sex;
    private double weightInGrams;
    private String kindOfHair;
    private int dogYears;
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private String chipNumber;
    private String breed;
    private String breedGroup;
    private List<DomesticatedDog> Litters;
    private boolean canSee;
    private boolean canHear;
    private Long parentId;
    private String dogStatus;
    @JsonIncludeProperties({"id", "firstName", "lastName", "street", "houseNumber", "houseNumberExtension", "zipCode", "city", "country"})
    private Person person;
}
