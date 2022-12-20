package nl.novi.breedsoft.dto;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.utility.ValueOfEnum;
import java.time.LocalDate;
import java.util.List;

@Data
public class PersonInputDto {

    //We do not have to know everything about a Person for the purpose of this application
    @NotEmpty(message = "Please enter a firstname")
    private String firstName;

    @NotEmpty(message = "Please enter a lastname")
    private String lastName;

    @NotEmpty(message = "Please enter a sex")
    @ValueOfEnum(enumClass = Sex.class, message = "Invalid sex")
    private String sex;

    @NotNull
    private LocalDate dateOfBirth;

    @NotEmpty(message = "Please enter a street name")
    private String street;

    @NotNull(message = "Please enter a house number")
    private int houseNumber;

    //Can be empty
    private String houseNumberExtension;

    //Zipcode must follow Dutch zipcode rules
    @NotEmpty(message = "Please enter a zipcode")
    @Pattern(regexp = "[1-9][0-9]{3}?[A-Z]{2}", message = "Format zipcode as: 1111AA")
    private String zipCode;

    @NotEmpty(message = "Please enter a city")
    private String city;

    @NotEmpty(message = "Please enter a country")
    private String country;

    @JsonIncludeProperties({"id", "name", "sex", "dateOfBirth", "breed", "kindOfHair", "haircolor"})
    private List<Dog> dogs;
}
