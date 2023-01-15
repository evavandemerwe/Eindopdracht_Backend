package nl.novi.breedsoft.dto.veterinarianAppointmentDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import java.time.LocalDate;
@Data
public class VeterinarianAppointmentOutputDto {
    private Long id;
    private LocalDate appointmentDate;
    private String subject;
    @JsonIncludeProperties({"id", "name", "sex", "dateOfBirth", "breed", "kindOfHair"})
    private DomesticatedDogOutputDto domesticatedDog;
}
