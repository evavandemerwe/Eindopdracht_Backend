package nl.novi.breedsoft.service;

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

    public DogOutputDto getDogById(Long id) {

        if (dogRepository.findById(id).isPresent()){
            Dog dog = dogRepository.findById(id).get();
            DogOutputDto dogOutputDto = transferToDto(dog);
            return dogOutputDto;
        } else {
            throw new RecordNotFoundException("Dog not found in database");
        }
    }

    public List<DogOutputDto> transferDogListToDtoList(List<Dog> dogs){
        List<DogOutputDto> dogDtoList = new ArrayList<>();

        for(Dog dog : dogs) {
            DogOutputDto dto = transferToDto(dog);
            dogDtoList.add(dto);
        }
        return dogDtoList;
    }

    public DogOutputDto transferToDto(Dog dog){

        DogOutputDto dogDto = new DogOutputDto();
        dogDto.id = dog.getId();
        dogDto.name = dog.getName();
        dogDto.chipnumber = dog.getChipnumber();
        dogDto.dogYears = dog.getDogYears();
        dogDto.breed = dog.getBreed();
        dogDto.breedGroup = dog.getBreedGroup();

        return dogDto;
    }
}
