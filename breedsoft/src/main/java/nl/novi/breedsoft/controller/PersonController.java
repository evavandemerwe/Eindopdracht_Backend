package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.PersonInputDto;
import nl.novi.breedsoft.dto.PersonOutputDto;
import nl.novi.breedsoft.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;

@RestController
@RequestMapping("persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService service){
        this.personService = service;
    }

    //Get mapping to get all persons from the database
    @GetMapping("")
    public ResponseEntity<Iterable<PersonOutputDto>> getAllPersons(){
        return ResponseEntity.ok(personService.getAllPersons());
    }

    //Get mapping to get one person by id from the database
    @GetMapping("/findbyid")
    public ResponseEntity<PersonOutputDto> getPersonById(@RequestParam("id") Long id) {

        PersonOutputDto personOutputDto = personService.getPersonById(id);

        return ResponseEntity.ok().body(personOutputDto);
    }

    //Get mapping to get one person by last name from the database
    @GetMapping("/findbyname")
    public ResponseEntity<Object> getPersonByName(@RequestParam("lastName") String lastName) {

        List<PersonOutputDto> personOutputDtoList = personService.getPersonByName(lastName);

        return ResponseEntity.ok().body(personOutputDtoList);

    }

    //Create a new person in the database
    @PostMapping("")
    public ResponseEntity<Object> createPerson (@Valid @RequestBody PersonInputDto personInputDto, BindingResult br) {
        //If there is an error in the binding
        if(br.hasErrors()){
            return bindingResultError(br);
        }else{
            //Person is created, return new person URI
            Long createdId = personService.createPerson(personInputDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/persons/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Person is successfully created!");
        }
    }
}
