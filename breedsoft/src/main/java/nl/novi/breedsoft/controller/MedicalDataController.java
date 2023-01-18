package nl.novi.breedsoft.controller;

import jakarta.validation.Valid;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataInputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataOutputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataPatchDto;
import nl.novi.breedsoft.service.MedicalDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import static nl.novi.breedsoft.utility.createUriResponse.createUri;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;

@RestController
@RequestMapping("medicaldata")
public class MedicalDataController {
    private final MedicalDataService medicalDataService;

    public MedicalDataController(MedicalDataService medicalDataService) {
        this.medicalDataService = medicalDataService;
    }

    /**
     * GET method to get a list all medical data from the database
     * @return ResponseEntity with OK http status code and a list with all medical data
     */
    @GetMapping("")
    public ResponseEntity<Iterable<MedicalDataOutputDto>> getAllMedicalData() {
        return ResponseEntity.ok(medicalDataService.getAllMedicalData());
    }

    /**
     * GET method to get all medical data for one dog by dog id from the database
     * @param medicalDataId ID of the medical data for which information is requested
     * @return ResponseEntity with OK http status code and the requested medical data
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<Iterable<MedicalDataOutputDto>> getMedicalDataById(
            @PathVariable("id") Long medicalDataId
    ){
        return ResponseEntity.ok(medicalDataService.getMedicaLDataByDogId(medicalDataId));
    }

    /**
     * POST method to create new medical data in database
     * @param medicalDataInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with created http status code and URI pointing to the newly created entity,
     * or bindingResultError if there is an error in the binding
     */
    @PostMapping("")
    public ResponseEntity<Object> createMedicalData(
            @Valid @RequestBody MedicalDataInputDto medicalDataInputDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if(bindingResult.hasErrors()){
            return bindingResultError(bindingResult);
        } else {
            // Medical data is created, return new medical data uri
            Long createdId = medicalDataService.createMedicalData(medicalDataInputDto);
            URI uri = createUri(createdId, "/medicaldata/");
            return ResponseEntity.created(uri).body("Medical data is successfully created!");
        }
    }

    /**
     * PUT method to update (if medical data exists) or create (if medical data does not exist) medical data
     * @param medicalDataId the id of the medical data for which a request is made
     * @param medicalDataInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with OK http status code and the updated or created medical data
     * or bindingResultError if there is an error in the binding
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMedicalData(
            @PathVariable("id") Long medicalDataId,
            @Valid @RequestBody MedicalDataInputDto medicalDataInputDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if(bindingResult.hasErrors()){
            return bindingResultError(bindingResult);
        } else {
            return ResponseEntity.ok().body(medicalDataService.updateMedicalData(medicalDataId, medicalDataInputDto));
        }
    }

    /**
     * PATCH method that updates medical data only when the medical data exists in the database
     * @param medicalDataId the id of the medical data for which a request is made
     * @param medicalDataPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with OK http status code and the requested medical data
     * or bindingResultError if there is an error in the binding
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchMedicalData(
            @PathVariable("id") Long medicalDataId,
            @Valid @RequestBody MedicalDataPatchDto medicalDataPatchDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            MedicalDataOutputDto medicalDataOutputDto = medicalDataService.patchMedicalData(medicalDataId, medicalDataPatchDto);
            return ResponseEntity.ok().body(medicalDataOutputDto);
        }
    }

    /**
     * DELETE method to delete medical data from the database by id
     * @param medicalDataId the id of the medical data for which information is requested
     * @return ResponseEntity with no content http status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMedicalData(@PathVariable("id") Long medicalDataId){
        medicalDataService.deleteMedicalData(medicalDataId);
        return ResponseEntity.noContent().build();
    }

}
