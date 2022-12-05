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

    @GetMapping("/findbyid/")
    public ResponseEntity<DogOutputDto> getDogById(@RequestParam("id") Long id) {

        DogOutputDto dogOutputDto = dogService.getDogById(id);

        return ResponseEntity.ok().body(dogOutputDto);

    }

    @GetMapping("/findbyname/")
    public ResponseEntity<DogOutputDto> getDogByName(@RequestParam("name") String name) {

        DogOutputDto dogOutputDto = dogService.getDogByName(name);

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


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDog(@PathVariable Long id, @Valid @RequestBody DogInputDto dogInputDto) {
        Object dogOutputDto = dogService.updateDog(id, dogInputDto);
        return ResponseEntity.ok().body(dogOutputDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DogOutputDto> patchDog(@PathVariable Long id,@Valid @RequestBody DogInputDto dogInputDto){

        DogOutputDto dogOutputDto = dogService.patchDog(id, dogInputDto);
        return ResponseEntity.ok().body(dogOutputDto);

    }


}
