package nl.novi.breedsoft.dto.personDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.DomesticatedDog;

import java.util.List;


@Data
public class PersonOutputDto {
    //We do not have to show everything in Person database for the purpose of this application
    private Long id;
    private String firstName;
    private String lastName;
    private Sex sex;
    private int age;
    private String street;
    private int houseNumber;
    private String houseNumberExtension;
    private String zipCode;
    private String city;
    private String country;

    @JsonIncludeProperties({"id", "name", "sex", "dateOfBirth", "breed", "kindOfHair", "haircolor"})
    private List<DomesticatedDog> dogs;

    public List<DomesticatedDog> getDogs() {
        return dogs;
    }

    public void setDogs(List<DomesticatedDog> dogs) {
        this.dogs = dogs;
    }
}