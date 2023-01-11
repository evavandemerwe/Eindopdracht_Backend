package nl.novi.breedsoft.dto.veterinarianAppointmentDtos;

import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import java.time.LocalDate;

@Data
public class VeterinarianAppointmentPatchDto {
    private Long id;
    private LocalDate appointmentDate;
    private String subject;
    private DomesticatedDog domesticatedDog;
}
