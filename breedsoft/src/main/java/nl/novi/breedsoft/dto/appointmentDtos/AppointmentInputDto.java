package nl.novi.breedsoft.dto.appointmentDtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.Person;
import java.time.LocalDateTime;

@Data
public class AppointmentInputDto {
    @NotEmpty(message = "Date may not be empty")
    private LocalDateTime appointmentDateTime;
    @NotEmpty(message = "Reason of the appointment may not be empty")
    @Size(min = 3, message = "Subject must be at least 3 characters long")
    private String subject;
    @NotEmpty(message = "Appointer (veterinarian) may not be empty")
    private Person appointer;
    @NotEmpty(message = "Appointee (dog owner) may not be empty")
    private Person appointee;
    @NotEmpty(message = "Dog id must be set")
    private DomesticatedDog domesticatedDog;
}
