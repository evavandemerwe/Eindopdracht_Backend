package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.DogInputDto;
import nl.novi.breedsoft.dto.DogOutputDto;
import nl.novi.breedsoft.dto.DogPatchDto;
import nl.novi.breedsoft.exception.BadFileException;
import nl.novi.breedsoft.service.DogService;
import static nl.novi.breedsoft.utility.BindingResultErrorUtility.bindingResultError;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URI;
import java.util.List;

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

    //Get mapping to get one dog by id from the database
    @GetMapping("/findbyid/")
    public ResponseEntity<DogOutputDto> getDogById(@RequestParam("id") Long id) {

        DogOutputDto dogOutputDto = dogService.getDogById(id);

        return ResponseEntity.ok().body(dogOutputDto);

    }

    //Get mapping to get one dog by name from the database
    @GetMapping("/findbyname/")
    public ResponseEntity<Object> getDogByName(@RequestParam("name") String name) {

        List<DogOutputDto> dogOutputDtoList = dogService.getDogByName(name);

        return ResponseEntity.ok().body(dogOutputDtoList);
    }

    //Get all children from a dog by id
    @GetMapping("/{id}/getchildren/")
    public List<DogOutputDto> getChildrenById(@PathVariable("id") Long id) {
        List<DogOutputDto> children = dogService.getAllChildren(id);
        return children;
    }

    //Create a new dog in the database
    @PostMapping("")
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

    @PostMapping("/{id}/image")
    public ResponseEntity<DogOutputDto> uploadDogImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile image) {
        if(image.isEmpty()) {
            throw new BadFileException("The provided file is empty");
        }
        try {
            DogOutputDto dogOutputDto = dogService.storeDogImage(id, image);
            return ResponseEntity.ok().body(dogOutputDto);
        } catch (IOException ex){
            throw new BadFileException("The provided file is not a valid image");
        }
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteDogImage(@PathVariable("id") Long id) {
        dogService.deleteDogImage(id);
        return ResponseEntity.noContent().build();
    }

    //Delete a dog from the database by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDog(@PathVariable("id") Long id) {
        dogService.deleteDog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDog(@PathVariable("id") Long id, @Valid @RequestBody DogInputDto dogInputDto, BindingResult br) {
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            Object dogOutputDto = dogService.updateDog(id, dogInputDto);
            return ResponseEntity.ok().body(dogOutputDto);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchDog(@PathVariable("id") Long id, @Valid @RequestBody DogPatchDto dogPatchDto, BindingResult br){
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            DogOutputDto dogOutputDto = dogService.patchDog(id, dogPatchDto);
            return ResponseEntity.ok().body(dogOutputDto);
        }
    }
}

