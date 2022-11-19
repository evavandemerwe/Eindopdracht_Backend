package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
public class DogController {

    private ArrayList<Dog> dogs;

    public DogController(){
        dogs = new ArrayList<>();
        Dog Saar = new Dog();
        Saar.setName("Saartje");
        Saar.setBreed(Breed.Dachschund);
        Saar.setDateOfBirth(LocalDate.now().minusYears(5));
        dogs.add(Saar);
    }

    //Get mapping voor alle honden in de database
    @GetMapping("/dogs")
    public ResponseEntity<Object> getAllDogs(){
        if(dogs.size() >= 0) {
            return new ResponseEntity<>(dogs, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    //Get mapping voor een specifieke hond in een database op basis van ID
    @GetMapping("/dogs/{id}")
    public  ResponseEntity<Object> getOneDog(@PathVariable int id){
        if(id < dogs.size()){
            Dog result = dogs.get(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>("ID not found in database", HttpStatus.NOT_FOUND);
    }

    //Get mapping voor alle honden met de opgegeven naam in de database
    @GetMapping("/dogs/name={name}")
    public  ResponseEntity<Object> getDogsWithName(@PathVariable String name){
        ArrayList <Dog> result = new ArrayList<>();
            for(Dog dog : dogs) {
                if (dog.getName().equalsIgnoreCase(name)) {
                    result.add(dog);
                }
            }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //Post mapping voor het toevoegen van een nieuwe hond in de database
    @PostMapping("/dogs")
    public ResponseEntity<Dog> addDog(@RequestBody Dog dog) {
        if(dog != null) {
            dogs.add(dog);
            return new ResponseEntity<>(dog, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Put mapping voor aanpassen van een hond met het opgegeven id de een database
    @PutMapping("/dogs/{id}")
    public ResponseEntity<Object> editDog(@PathVariable int id, @RequestBody Dog dog) {
        if (id >= 0 && id < dogs.size()) {
            dogs.set(id, dog);
            return new ResponseEntity<>(dog, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid id", HttpStatus.BAD_REQUEST);
    }

    //Delete mapping voor de hond met het opgegeven id in de database
    @DeleteMapping("/dogs/{id}")
    public ResponseEntity<Object> removeDog(@PathVariable int id) {
        {
            if(id < dogs.size()){
                dogs.remove(id);
                return new ResponseEntity<>("Removed!", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("Id is not found in database", HttpStatus.BAD_REQUEST);
            }
        }
    }
}
