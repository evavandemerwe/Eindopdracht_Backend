package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.repository.DogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;

import java.net.URI;

@RestController
@RequestMapping("dogs")
public class DogController {

    @Autowired
    public DogRepository dogRepository;

    //Get mapping voor alle honden in de database
    @GetMapping("")
    public ResponseEntity<Iterable<Dog>> getAllDogs() {

        if (dogRepository.count() > 0) {
            return ResponseEntity.ok(dogRepository.findAll());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    //Get mapping voor een specifieke hond in een database op basis van ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneDog(@PathVariable long id) {
        if (dogRepository.existsById(id)) {

            return ResponseEntity.ok(dogRepository.findById(id));
        }
        return ResponseEntity.notFound().build();
    }

    //Get mapping voor alle honden met de opgegeven naam in de database
    @GetMapping("/name={name}")
    public ResponseEntity<Object> getDogsWithName(@PathVariable String name) {

        if (dogRepository.findByNameContaining(name) != null) {
            return ResponseEntity.ok(dogRepository.findByNameContaining(name));
        }
        return ResponseEntity.notFound().build();
    }

    //Post mapping voor het toevoegen van een nieuwe hond in de database
    @PostMapping("")
    public ResponseEntity<Object> addDog(@RequestBody Dog dog) {
        Dog savedDog = dogRepository.save(dog);

        //Teruggeven van het nieuwe resource path in de header
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/dogs/" + savedDog.getId())
                .toUriString());
        return ResponseEntity.created(uri).body("Dog created: " + savedDog.getName() + ".");
    }

    //Put mapping voor aanpassen van een hond met het opgegeven id de een database
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> editDog(@PathVariable long id, @RequestBody Dog dog) throws Exception {

            Dog updateDog = dogRepository.findById(id).orElseThrow(() -> new Exception("Dog does not exist with id: " + id));
            updateDog.setName(dog.getName());
            updateDog.setBreedGroup(dog.getBreedGroup());
            updateDog.setBreed(dog.getBreed());
            updateDog.setChipnumber(dog.getChipnumber());
            updateDog.setLitter(dog.getLitter());

            dogRepository.save(updateDog);

            return ResponseEntity.ok(updateDog.getName());
    }

    //Delete mapping voor de hond met het opgegeven id in de database
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeDog(@PathVariable long id) {
        {
            if(dogRepository.existsById(id)){
                dogRepository.deleteById(id);
                return ResponseEntity.ok("Dog deleted from database.");
            }else {
                return ResponseEntity.notFound().build();
            }
        }
    }
}
