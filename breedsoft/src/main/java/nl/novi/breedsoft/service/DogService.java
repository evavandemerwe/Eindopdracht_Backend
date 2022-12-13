package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.DogInputDto;
import nl.novi.breedsoft.dto.DogOutputDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.repository.DogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class DogService {
    private DogRepository dogRepository;

    //Constructor injection
    public DogService(DogRepository repo){
        this.dogRepository = repo;
    }

    //Output Dto is used for data from database
    //Get all dogs
    public List<DogOutputDto> getAllDogs() {
        List<Dog> dogList = dogRepository.findAll();
        return transferDogListToDtoList(dogList);
    }

    //Get one dog by ID
    public DogOutputDto getDogById(Long id) {
        if (dogRepository.findById(id).isPresent()){
            Dog dog = dogRepository.findById(id).get();
            DogOutputDto dogOutputDto = transferToOutputDto(dog);
            return dogOutputDto;
        } else {
            throw new RecordNotFoundException("Dog not found in database");
        }
    }

    //Get one dog by Name
    public DogOutputDto getDogByName(String name) {
        if (dogRepository.findByNameContaining(name) != null){
            Dog dog = dogRepository.findByNameContaining(name);
            DogOutputDto dogOutputDto = transferToOutputDto(dog);
            return dogOutputDto;
        } else {
            throw new RecordNotFoundException("Dog not found in database");
        }
    }


    public Long createDog(DogInputDto dogInputDto){

        Dog dog = transferToDog(dogInputDto);
        dogRepository.save(dog);

        return dog.getId();

    }

    public void deleteDog(@RequestBody Long id) {

        if (dogRepository.findById(id).isPresent()){
               dogRepository.deleteById(id);
        } else {
            throw new  RecordNotFoundException("No dogs with given ID found.");
        }
    }

    //PUT sends an enclosed entity of a resource to the server.
    //If the entity already exists, the server updates its data. Otherwise, the server creates a new entity
    public Object updateDog(Long id, DogInputDto dogInputDto) {

        if (dogRepository.findById(id).isPresent()){
            Dog dog = dogRepository.findById(id).get();

            Dog updatedDog = transferToDog(dogInputDto);
            //Keeping the former id, as we will update the existing dog
            updatedDog.setId(dog.getId());

            dogRepository.save(updatedDog);

            return transferToOutputDto(updatedDog);

        } else {
            createDog(dogInputDto);
            return dogInputDto;
        }

    }

    //PATCH will only update an existing object,
    //with the properties mapped in the request body (that are not null).
    public DogOutputDto patchDog(long id, DogInputDto dogInputDto) {
        Optional<Dog> dogFound = dogRepository.findById(id);

        if (dogFound.isPresent()) {

            Dog updatedDog = dogRepository.getReferenceById(id);
            if (dogInputDto.getName() != null) {
                updatedDog.setName(dogInputDto.getName());
            }
            if (dogInputDto.getHairColor() != null) {
                updatedDog.setHairColor(dogInputDto.getHairColor());
            }
            if (dogInputDto.getFood() != null) {
                updatedDog.setFood(dogInputDto.getFood());
            }
            if (dogInputDto.getSex() != null) {
                updatedDog.setSex(Sex.valueOf(dogInputDto.getSex()));
            }
            if (dogInputDto.getWeightInGrams() > 0.0) {
                updatedDog.setWeightInGrams(dogInputDto.getWeightInGrams());
            }
            if (dogInputDto.getKindOfHair() != null) {
                updatedDog.setKindOfHair(dogInputDto.getKindOfHair());
            }
            if (dogInputDto.getDateOfBirth() != null) {
                updatedDog.setDateOfBirth(dogInputDto.getDateOfBirth());
            }
            if (dogInputDto.getDateOfDeath() != null) {
                updatedDog.setDateOfDeath(dogInputDto.getDateOfDeath());
            }
            if (dogInputDto.getFood() != null) {
                updatedDog.setFood(dogInputDto.getFood());
            }
            if (dogInputDto.getBreed() != null) {
                updatedDog.setBreed(Breed.valueOf(dogInputDto.getBreed()));
            }
            if (dogInputDto.getBreedGroup() != null) {
                updatedDog.setBreedGroup(BreedGroup.valueOf(dogInputDto.getBreedGroup()));
            }
            if (dogInputDto.getChipNumber() != null) {
                updatedDog.setChipnumber(dogInputDto.getChipNumber());
            }

            dogRepository.save(updatedDog);

            return transferToOutputDto(updatedDog);

        } else {

            throw new RecordNotFoundException("Dog is not found.");

        }
    }



    //DTO helper classes
    public List<DogOutputDto> transferDogListToDtoList(List<Dog> dogs){
        List<DogOutputDto> dogDtoList = new ArrayList<>();

        for(Dog dog : dogs) {
            DogOutputDto dto = transferToOutputDto(dog);
            dogDtoList.add(dto);
        }
        return dogDtoList;
    }

    public DogOutputDto transferToOutputDto(Dog dog){

        DogOutputDto dogDto = new DogOutputDto();
        dogDto.setId(dog.getId());
        dogDto.setName(dog.getName());
        dogDto.setHairColor(dog.getHairColor());
        dogDto.setFood(dog.getFood());
        dogDto.setSex(dog.getSex());
        dogDto.setWeightInGrams(dog.getWeightInGrams());
        dogDto.setKindOfHair(dog.getKindOfHair());
        dogDto.setDogYears(dog.getDogYears());
        dogDto.setDateOfBirth(dog.getDateOfBirth());
        dogDto.setDateOfDeath(dog.getDateOfDeath());
        dogDto.setChipNumber(dog.getChipnumber());
        dogDto.setBreed(dog.getBreed());
        dogDto.setBreedGroup(dog.getBreedGroup());

        return dogDto;
    }

    public Dog transferToDog(DogInputDto dto){
        Dog dog = new Dog();

        dog.setName(dto.getName());
        dog.setHairColor(dto.getHairColor());
        dog.setFood(dto.getFood());
        dog.setSex(Sex.valueOf(dto.getSex()));
        dog.setWeightInGrams(dto.getWeightInGrams());
        dog.setKindOfHair(dto.getKindOfHair());
        dog.setDogYears(dto.getDogYears());
        dog.setDateOfBirth(dto.getDateOfBirth());
        dog.setDateOfDeath(dto.getDateOfDeath());
        dog.setChipnumber(dto.getChipNumber());
        dog.setBreed(Breed.valueOf(dto.getBreed()));
        dog.setDogYears(dto.getDogYears());
        dog.setBreedGroup(BreedGroup.valueOf(dto.getBreedGroup()));
        dog.setLitter(dto.getLitter());

        return dog;
    }
}
