package nl.novi.breedsoft.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.enumerations.Sex;


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

    @JsonIncludeProperties("id")
    private Dog dog;
}
