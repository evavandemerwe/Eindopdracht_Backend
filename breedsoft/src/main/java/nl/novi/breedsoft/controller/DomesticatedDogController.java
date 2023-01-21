package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogPatchDto;
import nl.novi.breedsoft.exception.BadFileException;
import nl.novi.breedsoft.service.DomesticatedDogService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import static nl.novi.breedsoft.utility.createUriResponse.createUri;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;
@RestController
@RequestMapping("dogs")
public class DomesticatedDogController {

    private final DomesticatedDogService domesticatedDogService;

    public DomesticatedDogController(DomesticatedDogService service){
        this.domesticatedDogService = service;
    }

    /**
     * GET method to get all dogs from the database
     * @return ResponseEntity with OK http status code and a list with all dogs
     */
    @GetMapping("")
    public ResponseEntity<Iterable<DomesticatedDogOutputDto>> getAllDomesticatedDogs() {
        List <DomesticatedDogOutputDto> domesticatedDogOutputDtoList = domesticatedDogService.getAllDomesticatedDogs();
        return ResponseEntity.ok(domesticatedDogOutputDtoList);
    }

    /**
     * GET method to get one dog by id from the database
     * @param domesticatedDogId ID of the dog for which information is requested
     * @return ResponseEntity with OK http status code and the requested domesticated Dog
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<DomesticatedDogOutputDto> getDomesticatedDogById(
            @PathVariable("id") Long domesticatedDogId
    ){
        DomesticatedDogOutputDto domesticatedDogOutputDto = domesticatedDogService.getDomesticatedDogById(domesticatedDogId);
        return ResponseEntity.ok().body(domesticatedDogOutputDto);
    }

    /**
     * Get mapping to get one dog by name from the database
     * @param name of the dog or dogs for which information is requested
     * @return ResponseEntity with OK http status code and a list with all dogs with the given name
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Object> getDomesticatedDogByName(@PathVariable("name") String name) {
        List<DomesticatedDogOutputDto> domesticatedDogOutputDtoList = domesticatedDogService.getDomesticatedDogByName(name);
        return ResponseEntity.ok().body(domesticatedDogOutputDtoList);
    }

    /**
     * GET method to get all children from a dog by id
     * @param domesticatedDogId ID of the dog for which information is requested
     * @return ResponseEntity with OK http status code and a list with all children of the dog
     */
    @GetMapping("/{id}/children")
    public ResponseEntity<List<DomesticatedDogOutputDto>> getChildrenById(
            @PathVariable("id") Long domesticatedDogId
    ){
        List<DomesticatedDogOutputDto> domesticatedDogOutputDtoList = domesticatedDogService.getAllChildren(domesticatedDogId);
        return ResponseEntity.ok().body(domesticatedDogOutputDtoList);
    }

    /**
     * GET method to get the parent of a dog by id
     * @param domesticatedDogId ID of the dog for which information is requested
     * @return ResponseEntity with OK http status code and the requested domesticated Dog
     */
    @GetMapping("/{id}/parent")
    public ResponseEntity<DomesticatedDogOutputDto> getParentById(
            @PathVariable("id") Long domesticatedDogId
    ){
        DomesticatedDogOutputDto domesticatedDogOutputDto = domesticatedDogService.getParentDog(domesticatedDogId);
        return ResponseEntity.ok().body(domesticatedDogOutputDto);
    }

    /**
     * GET method to get all available dogs from the database
     * @return ResponseEntity with OK http status code and a list with requested domesticated Dogs
     */
        @GetMapping("/available")
    public ResponseEntity<List<DomesticatedDogOutputDto>> getAvailableDogs() {
        List<DomesticatedDogOutputDto> availableDogs = domesticatedDogService.getAvailableDomesticatedDogs();
        return ResponseEntity.ok().body(availableDogs);
    }

    /**
     * GET method to get all breed dogs from the database
     * @return ResponseEntity with OK http status code and a list with requested domesticated Dogs
     */
    @GetMapping("/breeddogs")
    public ResponseEntity<List<DomesticatedDogOutputDto>> getAllBreedDogs(){
        List<DomesticatedDogOutputDto> breedDogs = domesticatedDogService.getDomesticatedBreedDogs();
        return ResponseEntity.ok().body(breedDogs);
    }

    /**
     * POST method to create a new dog
     * @param domesticatedDogInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with created http status code and URI pointing to the newly created entity,
     * or bindingResultError if there is an error in the binding
     */
    @PostMapping("")
    public ResponseEntity<Object> createDomesticatedDog(
            @Valid @RequestBody DomesticatedDogInputDto domesticatedDogInputDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
           return bindingResultError(bindingResult);
        } else {
            // Dog is created, return new dog URI
            Long createdId = domesticatedDogService.createDomesticatedDog(domesticatedDogInputDto);
            URI uri = createUri(createdId, "/dogs/");
            return ResponseEntity.created(uri).body("Dog is successfully created!");
        }
    }

    /**
     * POST method to create a new litter (list of dogs)
     * @param domesticatedDogId ID of the dog for which information is requested
     * @param domesticatedDogInputDtoList Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with OK http status code and a list with all created dogs
     */
    @PostMapping("/{id}/children")
    public ResponseEntity<Object> uploadLitter(
            @PathVariable("id") Long domesticatedDogId,
            @Valid @RequestBody List<DomesticatedDogInputDto> domesticatedDogInputDtoList,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            List<DomesticatedDogOutputDto> domesticatedDogOutputDtoList = domesticatedDogService.createLitterList(domesticatedDogInputDtoList, domesticatedDogId);
            return ResponseEntity.ok().body(domesticatedDogOutputDtoList);
        }
    }

    /**
     * POST method to upload a picture for a dog
     * @param domesticatedDogId ID of the person for which information is requested
     * @param image Picture of the dog
     * @return RResponseEntity with created http status code and URI pointing to the newly created entity,
     * or BadFileException if there is an error with the file
     */
    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadDogImage(
            @PathVariable("id") Long domesticatedDogId,
            @RequestParam("image") MultipartFile image
    ) {
        if(image.isEmpty()) {
            throw new BadFileException("The provided file is empty");
        }
        try {
            Long createdId = domesticatedDogService.storeDogImage(domesticatedDogId, image);
            URI uri = createUri(createdId, "/dogs/");
            return ResponseEntity.created(uri).body("Picture is successfully uploaded!");
        } catch (IOException ex){
            throw new BadFileException("The provided file is not a valid image");
        }
    }

    /**
     * PUT method to update (if dog exists) or create (if dog does not exist) a dog
     * @param domesticatedDogId ID of the person for which information is put
     * @param domesticatedDogInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with OK http status code and the updated or created dog
     * or bindingResultError if there is an error in the binding
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDog(
            @PathVariable("id") Long domesticatedDogId,
            @Valid @RequestBody DomesticatedDogInputDto domesticatedDogInputDto,
            BindingResult bindingResult
    ) {
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            Object dogOutputDto = domesticatedDogService.updateDomesticatedDog(domesticatedDogId, domesticatedDogInputDto);
            return ResponseEntity.ok().body(dogOutputDto);
        }
    }

    /**
     * PATCH method that updates a dog only when the dog exists in the database
     * @param domesticatedDogId ID of the person for which information is put
     * @param domesticatedDogPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with OK http status code and String message,
     * or bindingResultError if there is an error in the binding
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchDomesticatedDog(
            @PathVariable("id") Long domesticatedDogId,
            @Valid @RequestBody DomesticatedDogPatchDto domesticatedDogPatchDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            String response = domesticatedDogService.patchDomesticatedDog(domesticatedDogId, domesticatedDogPatchDto);
            return ResponseEntity.ok().body(response);
        }
    }

    /**
     * DELETE method to delete an image from the database by id
     * @param domesticatedDogId ID of the person for which information is requested
     * @return ResponseEntity with no content http status code
     */
    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteDogImage(@PathVariable("id") Long domesticatedDogId) {
        domesticatedDogService.deleteDogImage(domesticatedDogId);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE method to delete a dog from the database by id
     * @param domesticatedDogId ID of the person for which information is requested
     * @return ResponseEntity with no content http status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDog(@PathVariable("id") Long domesticatedDogId) {
        domesticatedDogService.deleteDomesticatedDog(domesticatedDogId);
        return ResponseEntity.noContent().build();
    }

}

