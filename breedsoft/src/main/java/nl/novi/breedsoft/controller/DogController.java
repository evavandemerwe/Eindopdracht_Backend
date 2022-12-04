package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.DogInputDto;
import nl.novi.breedsoft.dto.DogOutputDto;
import nl.novi.breedsoft.service.DogService;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;




@RestController
@RequestMapping("dogs")
public class DogController {

    private final DogService dogService;

    public DogController(DogService service){
        this.dogService = service;
    }

    //Get mapping to get all dogs from the database
    @GetMapping("")
    public ResponseEntity<Iterable<DogOutputDto>> getAllDogs() {
        return ResponseEntity.ok(dogService.getAllDogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogOutputDto> getDogById(@PathVariable("id") Long id) {

        DogOutputDto dogOutputDto = dogService.getDogById(id);

        return ResponseEntity.ok().body(dogOutputDto);

    }

    @PostMapping("")
    public ResponseEntity<Object> createDog(@Valid @RequestBody DogInputDto dogInputDto, BindingResult br) {

        if (br.hasErrors()) {
            // something's wrong
           return bindingResultError(br);
        } else {
            // Dog is created, return new dog URI
            Long createdId = dogService.createDog(dogInputDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/dogs/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Dog is successfully created!");
        }
    }
}
