package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.DogOutputDto;
import nl.novi.breedsoft.service.DogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

}
