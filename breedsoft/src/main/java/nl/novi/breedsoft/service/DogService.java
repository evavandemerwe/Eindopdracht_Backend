package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.DogInputDto;
import nl.novi.breedsoft.dto.DogOutputDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.Dog;
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
            if (dogInputDto.name != null) {
                updatedDog.setName(dogInputDto.name);
            }
            if (dogInputDto.breed != null) {
                updatedDog.setBreed(dogInputDto.breed);
            }
            if (dogInputDto.breedGroup != null) {
                updatedDog.setBreedGroup(dogInputDto.breedGroup);
            }
            if (dogInputDto.chipnumber != null) {
                updatedDog.setChipnumber(dogInputDto.chipnumber);
            }

            //Check on empty value has already been done in DTO
            updatedDog.setDogYears(dogInputDto.dogYears);


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
        dogDto.id = dog.getId();
        dogDto.name = dog.getName();
        dogDto.chipnumber = dog.getChipnumber();
        dogDto.dogYears = dog.getDogYears();
        dogDto.breed = dog.getBreed();
        dogDto.breedGroup = dog.getBreedGroup();

        return dogDto;
    }

    public Dog transferToDog(DogInputDto dto){
        Dog dog = new Dog();

        dog.setName(dto.name);
        dog.setChipnumber(dto.chipnumber);
        dog.setBreed(dto.breed);
        dog.setDogYears(dto.dogYears);
        dog.setBreedGroup(dto.breedGroup);
        dog.setLitter(dto.litter);

        return dog;
    }
}
