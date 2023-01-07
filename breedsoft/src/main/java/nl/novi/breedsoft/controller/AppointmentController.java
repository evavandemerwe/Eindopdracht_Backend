package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.appointmentDtos.AppointmentOutputDto;
import nl.novi.breedsoft.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(appointmentService.getAllAppointmentsForDogId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAppointment(@PathVariable("id") Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
