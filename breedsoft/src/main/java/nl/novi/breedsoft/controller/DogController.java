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
import java.util.List;

@RestController
@RequestMapping()
public class DogController {

    private final DogService dogService;

    public DogController(DogService service){
        this.dogService = service;
    }

    //Get mapping to get all dogs from the database
    @GetMapping("/dogs")
    public ResponseEntity<Iterable<DogOutputDto>> getAllDogs() {
        return ResponseEntity.ok(dogService.getAllDogs());
    }

    //Get mapping to get one dog by id from the database
    @GetMapping("/dogs/findbyid/")
    public ResponseEntity<DogOutputDto> getDogById(@RequestParam("id") Long id) {

        DogOutputDto dogOutputDto = dogService.getDogById(id);

        return ResponseEntity.ok().body(dogOutputDto);

    }

    //Get mapping to get one dog by name from the database
    @GetMapping("/dogs/findbyname/")
    public ResponseEntity<Object> getDogByName(@RequestParam("name") String name) {

        List<DogOutputDto> dogOutputDtoList = dogService.getDogByName(name);

        return ResponseEntity.ok().body(dogOutputDtoList);
    }

    //Create a new dog in the database
    @PostMapping("/dogs")
    public ResponseEntity<Object> createDog(@Valid @RequestBody DogInputDto dogInputDto, BindingResult br) {
        //If there is an error in the binding
        if (br.hasErrors()) {
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


    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<Object> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/dogs/{id}")
    public ResponseEntity<Object> updateDog(@PathVariable("id") Long id, @Valid @RequestBody DogInputDto dogInputDto, BindingResult br) {
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            Object dogOutputDto = dogService.updateDog(id, dogInputDto);
            return ResponseEntity.ok().body(dogOutputDto);
        }
    }

    //@Valid niet toegepast, omdat je op een PATCH alleen de velden mee geeft die aangepast moeten worden
    @PatchMapping("/dogs/{id}")
    public ResponseEntity<Object> patchDog(@PathVariable("id") Long id, @RequestBody DogInputDto dogInputDto){

        DogOutputDto dogOutputDto = dogService.patchDog(id, dogInputDto);
        return ResponseEntity.ok().body(dogOutputDto);
        
    }
}

