package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentInputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentOutputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentPatchDto;
import nl.novi.breedsoft.service.VeterinarianAppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;
import static nl.novi.breedsoft.utility.createUriResponse.createUri;

@RestController
@RequestMapping("appointments")
public class VeterinarianAppointmentController {
    private final VeterinarianAppointmentService veterinarianAppointmentService;

    public VeterinarianAppointmentController(VeterinarianAppointmentService veterinarianAppointmentService) {
        this.veterinarianAppointmentService = veterinarianAppointmentService;
    }

    /**
     * GET method to get a list of all appointments from the database
     * @return ResponseEntity with OK http status code and a list with all appointments
     */
    @GetMapping("")
    public ResponseEntity<Iterable<VeterinarianAppointmentOutputDto>> getAllAppointments() {
        return ResponseEntity.ok(veterinarianAppointmentService.getAllAppointments());
    }

    /**
     * GET method to get a verterinarian appointment by dog id
     * @param dogId ID of the dog for which information is requested
     * @return ResponseEntity with OK http status code and a list with the requested appointment(s)
     */
    @GetMapping("/dogid/{id}")
    public ResponseEntity<Iterable<VeterinarianAppointmentOutputDto>> getAllAppointmentsForDogId(@PathVariable("id") Long dogId){
        return ResponseEntity.ok(veterinarianAppointmentService.getAllAppointmentsByDogId(dogId));
    }

    /**
     * POST method to create a new veterinarian appointment
     * @param veterinarianAppointmentInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with created http status code and URI pointing to the newly created entity,
     * or bindingResultError if there is an error in the binding
     */
    @PostMapping("")
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody VeterinarianAppointmentInputDto veterinarianAppointmentInputDto, BindingResult bindingResult){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            //VeterinarianAppointment is created, return new appointment id
            Long createdId = veterinarianAppointmentService.createVeterinarianAppointment(veterinarianAppointmentInputDto);
            URI uri = createUri(createdId, "/appointments/");
            return ResponseEntity.created(uri).body("VeterinarianAppointment is successfully created!");
        }
    }

    /**
     * PUT method to update (if appointment exists) or create (if appointment does not exist) a veterinarian appointment
     * @param appointmentId ID of the appointment for which information is requested
     * @param veterinarianAppointmentInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with ok http status code and updated or created appointment,
     * or bindingResultError if there is an error in the binding
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateVeterinarianAppointment(@PathVariable("id") Long appointmentId, @Valid @RequestBody VeterinarianAppointmentInputDto veterinarianAppointmentInputDto, BindingResult bindingResult){
        //If there is an error in the binding
        if(bindingResult.hasErrors()){
            return bindingResultError(bindingResult);
        } else {
            return ResponseEntity.ok().body(veterinarianAppointmentService.updateVeterinarianAppointment(appointmentId, veterinarianAppointmentInputDto));
        }
    }

    /**
     * PATCH method that updates an appointment only when the appointment exists in the database
     * @param appointmentId ID of the appointment for which information is requested
     * @param veterinarianAppointmentPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with OK http status code and the updated appointment,
     * or bindingResultError if there is an error in the binding
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchVeterinarianAppointment(@PathVariable("id") Long appointmentId, @Valid @RequestBody VeterinarianAppointmentPatchDto veterinarianAppointmentPatchDto, BindingResult bindingResult){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            VeterinarianAppointmentOutputDto veterinarianAppointmentOutputDto = veterinarianAppointmentService.patchVeterinarianAppointment(appointmentId, veterinarianAppointmentPatchDto);
            return ResponseEntity.ok().body(veterinarianAppointmentOutputDto);
        }
    }

    /**
     * DELETE method to delete an appointment from the database by id
     * @param appointmentId ID of the appointment for which information is requested
     * @return ResponseEntity with no content http status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAppointment(@PathVariable("id") Long appointmentId) {
        veterinarianAppointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }
}
