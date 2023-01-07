package nl.novi.breedsoft.dto.appointmentDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.Person;

import java.time.LocalDateTime;

@Data
public class AppointmentPatchDto {
    private Long id;
    private LocalDateTime appointmentDateTime;
    private String subject;
    @JsonIncludeProperties({"id", "firstname", "lastname"})
    private Person appointer;
    @JsonIncludeProperties({"id", "firstname", "lastname"})
    private Person appointee;
    @JsonIncludeProperties({"id", "name"})
    private DomesticatedDog domesticatedDog;
}
