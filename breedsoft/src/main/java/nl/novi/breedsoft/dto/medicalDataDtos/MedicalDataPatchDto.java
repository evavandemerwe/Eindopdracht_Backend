package nl.novi.breedsoft.dto.medicalDataDtos;

import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import java.time.LocalDate;

@Data
public class MedicalDataPatchDto {
    private Long id;
    private LocalDate dateOfMedicalTreatment;
    private String medicine;
    private String diagnose;
    private String treatment;
    private DomesticatedDog domesticatedDog;
}
