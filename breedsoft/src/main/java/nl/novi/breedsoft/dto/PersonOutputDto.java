package nl.novi.breedsoft.dto;

import lombok.Data;
import nl.novi.breedsoft.model.animal.enumerations.Sex;


@Data
public class PersonOutputDto {
    //We do not have to show everything in Person database for the purpose of this application
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
}
