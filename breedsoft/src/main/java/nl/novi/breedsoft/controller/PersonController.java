package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.PersonOutputDto;
import nl.novi.breedsoft.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService service){
        this.personService = service;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<PersonOutputDto>> getAllPersons(){
        return ResponseEntity.ok(personService.getAllPersons());
    }
}
