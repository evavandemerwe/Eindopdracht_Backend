package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogPatchDto;
import nl.novi.breedsoft.exception.BadFileException;
import nl.novi.breedsoft.service.DomesticatedDogService;
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
public class DomesticatedDogController {

    private final DomesticatedDogService domesticatedDogService;

    public DomesticatedDogController(DomesticatedDogService service){
        this.domesticatedDogService = service;
    }

    //Get mapping to get all dogs from the database
    @GetMapping("")
    public ResponseEntity<Iterable<DomesticatedDogOutputDto>> getAllDomesticatedDogs() {
        List <DomesticatedDogOutputDto> domesticatedDogOutputDtoList = domesticatedDogService.getAllDomesticatedDogs();
        return ResponseEntity.ok(domesticatedDogOutputDtoList);
    }

    //Get mapping to get one dog by id from the database
    @GetMapping("/findbyid/")
    public ResponseEntity<DomesticatedDogOutputDto> getDomesticatedDogById(@RequestParam("id") Long id) {

        DomesticatedDogOutputDto domesticatedDogOutputDto = domesticatedDogService.getDomesticatedDogById(id);

        return ResponseEntity.ok().body(domesticatedDogOutputDto);

    }

    //Get mapping to get one dog by name from the database
    @GetMapping("/findbyname")
    public ResponseEntity<Object> getDomesticatedDogByName(@RequestParam("name") String name) {

        List<DomesticatedDogOutputDto> domesticatedDogOutputDtoList = domesticatedDogService.getDomesticatedDogByName(name);

        return ResponseEntity.ok().body(domesticatedDogOutputDtoList);
    }

    //Get all children from a dog by id
    @GetMapping("/{id}/children")
    public List<DomesticatedDogOutputDto> getChildrenById(@PathVariable("id") Long id) {
        List<DomesticatedDogOutputDto> children = domesticatedDogService.getAllChildren(id);
        return children;
    }

    @GetMapping("/{id}/parent")
    public DomesticatedDogOutputDto getParentById(@PathVariable("id") Long id) {
        DomesticatedDogOutputDto parent = domesticatedDogService.getMotherDog(id);
        return parent;
    }
    @GetMapping("/available")
    public ResponseEntity<List<DomesticatedDogOutputDto>> getAvailableDogs() {
        List<DomesticatedDogOutputDto> availableDogs = domesticatedDogService.getAvailableDomesticatedDogs();
        return ResponseEntity.ok().body(availableDogs);
    }

    @GetMapping("/breeddogs")
    public ResponseEntity<List<DomesticatedDogOutputDto>> getAllBreedDogs(){
        List<DomesticatedDogOutputDto> breedDogs = domesticatedDogService.getDomesticatedBreedDogs();
        return ResponseEntity.ok().body(breedDogs);
    }

    //Create a new dog in the database
    @PostMapping("")
    public ResponseEntity<Object> createDomesticatedDog(@Valid @RequestBody DomesticatedDogInputDto domesticatedDogInputDto, BindingResult br) {
        //If there is an error in the binding
        if (br.hasErrors()) {
           return bindingResultError(br);
        } else {
            // Dog is created, return new dog URI
            Long createdId = domesticatedDogService.createDomesticatedDog(domesticatedDogInputDto);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/dogs/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Dog is successfully created!");
        }
    }

    @PostMapping("/{id}/children")
    public Object uploadLitter(@PathVariable("id") Long id, @Valid @RequestBody List<DomesticatedDogInputDto> domesticatedDogInputDtoList, BindingResult br){
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            List<DomesticatedDogOutputDto> createdDogsList = domesticatedDogService.createLitterList(domesticatedDogInputDtoList, id);
            return createdDogsList;
        }
    }

    @PostMapping("/{id}/image")
    public ResponseEntity<Object> uploadDogImage(@PathVariable("id") Long id, @RequestParam("image") MultipartFile image) {
        if(image.isEmpty()) {
            throw new BadFileException("The provided file is empty");
        }
        try {
            Long createdId = domesticatedDogService.storeDogImage(id, image);

            URI uri = URI.create(
                    ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/dogs/" + createdId).toUriString());
            return ResponseEntity.created(uri).body("Picture is successfully uploaded!");

        } catch (IOException ex){
            throw new BadFileException("The provided file is not a valid image");
        }
    }

    @DeleteMapping("/{id}/image")
    public ResponseEntity<Object> deleteDogImage(@PathVariable("id") Long id) {
        domesticatedDogService.deleteDogImage(id);
        return ResponseEntity.noContent().build();
    }

    //Delete a dog from the database by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDog(@PathVariable("id") Long id) {
        domesticatedDogService.deleteDomesticatedDog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDog(@PathVariable("id") Long id, @Valid @RequestBody DomesticatedDogInputDto domesticatedDogInputDto, BindingResult br) {
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            Object dogOutputDto = domesticatedDogService.updateDomesticatedDog(id, domesticatedDogInputDto);
            return ResponseEntity.ok().body(dogOutputDto);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchDomesticatedDog(@PathVariable("id") Long id, @Valid @RequestBody DomesticatedDogPatchDto domesticatedDogPatchDto, BindingResult br){
        //If there is an error in the binding
        if (br.hasErrors()) {
            return bindingResultError(br);
        } else {
            String response = domesticatedDogService.patchDomesticatedDog(id, domesticatedDogPatchDto);
            return ResponseEntity.ok().body(response);
        }
    }
}

