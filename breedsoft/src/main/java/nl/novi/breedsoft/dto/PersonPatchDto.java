package nl.novi.breedsoft.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;

import java.time.LocalDate;
import java.util.List;

@Data
public class PersonPatchDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String sex;
    private int age;
    private String street;
    private int houseNumber;
    private String houseNumberExtension;
    private String zipCode;
    private String city;
    private String country;
    private LocalDate dateOfBirth;

    @JsonIncludeProperties({"id", "name", "sex", "dateOfBirth", "breed", "kindOfHair", "haircolor"})
    private List<DomesticatedDog> dogs;
}
