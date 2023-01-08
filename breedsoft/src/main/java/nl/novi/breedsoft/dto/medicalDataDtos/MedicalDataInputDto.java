package nl.novi.breedsoft.dto.medicalDataDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;

import java.time.LocalDate;

@Data
public class MedicalDataInputDto {

    @Null
    private Long id;

    @NotNull(message = "Date may not be empty")
    private LocalDate dateOfMedicalTreatment;

    private String medicine;
    private String diagnose;
    private String treatment;

    @NotNull(message = "Dog ID must be given")
    private DomesticatedDog domesticatedDog;

}
