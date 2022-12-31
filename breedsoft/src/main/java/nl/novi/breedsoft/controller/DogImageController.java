package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.DogImageDto;
import nl.novi.breedsoft.dto.DogInputDto;
import nl.novi.breedsoft.exception.BadFileException;
import nl.novi.breedsoft.service.DogImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("images")
public class DogImageController {

    private final DogImageService dogImageService;

    public DogImageController(DogImageService dogImageService) {
        this.dogImageService = dogImageService;
    }

    //RequestParam contains the image in the form of a multipart file, which is used in Spring Boot for file transfer
    @PostMapping()
    @ExceptionHandler({IOException.class,BadFileException.class})
    public ResponseEntity<DogImageDto> uploadDogImage(@RequestParam("image") MultipartFile file) {
        try {
            DogImageDto dogImageDto = dogImageService.storeDogImage(file);
            return ResponseEntity.ok().body(dogImageDto);
        } catch (IOException ex){
            throw new BadFileException("The provided file is not a valid image");
        }
    }

    @GetMapping("")
    public ResponseEntity<Iterable<DogImageDto>> getAllImages() {
        return ResponseEntity.ok(dogImageService.getAllImages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DogImageDto> getDogImageById(@PathVariable Long id) {

        DogImageDto dogImageDto = dogImageService.getDogImageById(id);

        return ResponseEntity.ok().body(dogImageDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDog(@PathVariable("id") Long id) {
        dogImageService.deleteDogImage(id);
        return ResponseEntity.noContent().build();
    }
}
