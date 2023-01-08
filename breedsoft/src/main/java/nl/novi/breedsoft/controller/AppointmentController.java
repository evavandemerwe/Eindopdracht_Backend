package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.appointmentDtos.AppointmentInputDto;
import nl.novi.breedsoft.dto.appointmentDtos.AppointmentOutputDto;
import nl.novi.breedsoft.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;

@RestController
@RequestMapping("appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<AppointmentOutputDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/findbydogid/{id}")
    public ResponseEntity<Iterable<AppointmentOutputDto>> getAllAppointmentsForDogId(@PathVariable("id") Long id){
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByDogId(id));
    }

    @PostMapping("")
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody AppointmentInputDto appointmentInputDto, BindingResult br){
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            //Appointment is created, return new appointment id
            Long createdId = appointmentService.createAppointment(appointmentInputDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/appointments/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Appointment is successfully created!");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAppointment(@PathVariable("id") Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
