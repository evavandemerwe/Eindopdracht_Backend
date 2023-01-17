package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.personDtos.PersonInputDto;
import nl.novi.breedsoft.dto.personDtos.PersonOutputDto;
import nl.novi.breedsoft.dto.personDtos.PersonPatchDto;
import nl.novi.breedsoft.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;
import static nl.novi.breedsoft.utility.createUriResponse.createUri;

@RestController
@RequestMapping("persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService service) {
        this.personService = service;
    }

    /**
     * GET request to get all persons from the database
     * @return ResponseEntity with OK http status code and a list with all persons
     */
    @GetMapping("")
    public ResponseEntity<Iterable<PersonOutputDto>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    /**
     * GET method to get one person by id from the database
     * @param personId ID of the person for which information is requested
     * @return ResponseEntity with OK http status code and the requested person
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<PersonOutputDto> getPersonById(@PathVariable("id") Long personId) {
        PersonOutputDto personOutputDto = personService.getPersonById(personId);
        return ResponseEntity.ok().body(personOutputDto);
    }

    /**
     * GET method to get one person by last name from the database
     * @param lastName the lastname for which person information is requested
     * @return ResponseEntity with OK http status code and a list with all persons with given lastname
     */
    @GetMapping("/name/{lastName}")
    public ResponseEntity<Object> getPersonByName(@PathVariable("lastName") String lastName) {
        List<PersonOutputDto> personOutputDtoList = personService.getPersonByName(lastName);
        return ResponseEntity.ok().body(personOutputDtoList);
    }

    /**
     * GET method to get a list of all persons with one or more breed dogs
     * @return ResponseEntity with OK http status code and a list with all persons with a breed dog
     */
    @GetMapping("/dogbreeders")
    public ResponseEntity<List<PersonOutputDto>> getDogBreeders(){
        List<PersonOutputDto> dogBreeders = personService.getDogBreeders();
        return ResponseEntity.ok().body(dogBreeders);
    }

    /**
     * POST method to create a new person in the database
     * @param personInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with created http status code and URI pointing to the newly created entity,
     * or bindingResultError if there is an error in the binding
     */
    @PostMapping("")
    public ResponseEntity<Object> createPerson(
            @Valid @RequestBody PersonInputDto personInputDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            //Person is created, return new person URI
            Long createdId = personService.createPerson(personInputDto);
            URI uri = createUri(createdId, "/persons/");
            return ResponseEntity.created(uri).body("Person is successfully created!");
        }
    }

    /**
     * PUT method to update (if person exists) or create (if person does not exist) a person
     * @param personId ID of the person for which information is requested
     * @param personInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with ok http status code and updated or created person,
     * or bindingResultError if there is an error in the binding
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePerson(
            @PathVariable("id") Long personId,
            @Valid @RequestBody PersonInputDto personInputDto,
            BindingResult bindingResult
    ){
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            Object personOutputDto = personService.updatePerson(personId, personInputDto);
            return ResponseEntity.ok().body(personOutputDto);
        }
    }

    /**
     * PATCH method that updates a person only when the person exists in the database
     * @param personId ID of the person for which information is requested
     * @param personPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @param bindingResult a Spring object that holds the result of the validation and binding and contains errors that may have occurred
     * @return ResponseEntity with OK http status code and the updated person,
     * or bindingResultError if there is an error in the binding
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchPerson(@PathVariable("id") Long personId, @Valid @RequestBody PersonPatchDto personPatchDto, BindingResult bindingResult) {
        //If there is an error in the binding
        if (bindingResult.hasErrors()) {
            return bindingResultError(bindingResult);
        } else {
            PersonOutputDto personOutputDto = personService.patchPerson(personId, personPatchDto);
            return ResponseEntity.ok().body(personOutputDto);
        }
    }

    /**
     * DELETE method to delete a person from the database by id
     * @param personId ID of the person for which information is requested
     * @return ResponseEntity with no content http status code
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable("id") Long personId) {
        personService.deletePerson(personId);
        return ResponseEntity.noContent().build();
    }
}
