package nl.novi.breedsoft.dto.medicalDataDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import java.time.LocalDate;
@Data
public class MedicalDataOutputDto {
    private Long id;
    private LocalDate dateOfMedicalTreatment;
    private String medicine;
    private String diagnose;
    private String treatment;

    @JsonIncludeProperties({"id", "name", "sex", "dateOfBirth", "breed", "kindOfHair"})
    private DomesticatedDog domesticatedDog;
}
