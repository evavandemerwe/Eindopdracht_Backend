package nl.novi.breedsoft.dto.veterinarianAppointmentDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import java.time.LocalDate;

@Data
public class VeterinarianAppointmentInputDto {
    @NotNull(message = "Date may not be empty")
    private LocalDate appointmentDate;
    @NotEmpty(message = "Reason of the appointment may not be empty")
    @Size(min = 3, message = "Subject must be at least 3 characters long")
    private String subject;
    @NotNull(message = "Dog ID must be given")
    private DomesticatedDog domesticatedDog;
}
