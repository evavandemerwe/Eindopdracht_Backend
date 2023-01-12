package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentInputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentOutputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentPatchDto;
import nl.novi.breedsoft.service.VeterinarianAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;

@RestController
@RequestMapping("appointments")
public class VeterinarianAppointmentController {
    private final VeterinarianAppointmentService veterinarianAppointmentService;

    public VeterinarianAppointmentController(VeterinarianAppointmentService veterinarianAppointmentService) {
        this.veterinarianAppointmentService = veterinarianAppointmentService;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<VeterinarianAppointmentOutputDto>> getAllAppointments() {
        return ResponseEntity.ok(veterinarianAppointmentService.getAllAppointments());
    }

    @GetMapping("/findbydogid/{id}")
    public ResponseEntity<Iterable<VeterinarianAppointmentOutputDto>> getAllAppointmentsForDogId(@PathVariable("id") Long id){
        return ResponseEntity.ok(veterinarianAppointmentService.getAllAppointmentsByDogId(id));
    }

    @PostMapping("")
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody VeterinarianAppointmentInputDto veterinarianAppointmentInputDto, BindingResult br){
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            //VeterinarianAppointment is created, return new appointment id
            Long createdId = veterinarianAppointmentService.createVeterinarianAppointment(veterinarianAppointmentInputDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/appointments/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("VeterinarianAppointment is successfully created!");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVeterinarianAppointment(@PathVariable("id") Long id, @Valid @RequestBody VeterinarianAppointmentInputDto veterinarianAppointmentInputDto, BindingResult br){
        //If there is an error in the binding
        if(br.hasErrors()){
            return bindingResultError(br);
        } else {
            return ResponseEntity.ok().body(veterinarianAppointmentService.updateVeterinarianAppointment(id, veterinarianAppointmentInputDto));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchVeterinarianAppointment(@PathVariable("id") Long id, @Valid @RequestBody VeterinarianAppointmentPatchDto veterinarianAppointmentPatchDto, BindingResult br){
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            VeterinarianAppointmentOutputDto veterinarianAppointmentOutputDto = veterinarianAppointmentService.patchVeterinarianAppointment(id, veterinarianAppointmentPatchDto);
            return ResponseEntity.ok().body(veterinarianAppointmentOutputDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAppointment(@PathVariable("id") Long id) {
        veterinarianAppointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
