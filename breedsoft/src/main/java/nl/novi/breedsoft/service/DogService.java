package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.DogInputDto;
import nl.novi.breedsoft.dto.DogOutputDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.repository.DogRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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


    public Long createDog(DogInputDto dogInputDto){
        Dog dog = transferToDog(dogInputDto);
        dogRepository.save(dog);

        return dog.getId();

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
