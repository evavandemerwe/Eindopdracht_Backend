package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.DogInputDto;
import nl.novi.breedsoft.dto.DogOutputDto;
import nl.novi.breedsoft.dto.DogPatchDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.Person;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.repository.DogRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class DogService {
    private final DogRepository dogRepository;
    private final PersonRepository personRepository;

    //Constructor injection
    public DogService(
            DogRepository dogRepository,
            PersonRepository personRepository
    ){
        this.dogRepository = dogRepository;
        this.personRepository = personRepository;
    }

    //Output Dto is used for representing data from database to the user
    //Get all dogs
    public List<DogOutputDto> getAllDogs() {
        List<Dog> dogList = dogRepository.findAll();
        return transferDogListToDtoList(dogList);
    }

    //Get one dog by ID
    public DogOutputDto getDogById(Long id) {
        if (dogRepository.findById(id).isPresent()){
            Dog dog = dogRepository.findById(id).get();
            return transferToOutputDto(dog);
        } else {
            throw new RecordNotFoundException("Dog not found in database");
        }
    }

    //Get dog list by Name
    public List<DogOutputDto> getDogByName(String name) {
        if (dogRepository.findByNameContaining(name) != null){
            List<Dog> dogList = dogRepository.findByNameContaining(name);
            List<DogOutputDto> dogOutputDtosList = new ArrayList<>();
            for(Dog dog : dogList){
                dogOutputDtosList.add(transferToOutputDto(dog));
            }
            return dogOutputDtosList;
        } else {
            throw new RecordNotFoundException("No dog with this name found in database");
        }
    }

    //Create creates a new entity in the database
    public Long createDog(DogInputDto dogInputDto){

        //Check if a dog owner is given
        if(dogInputDto.getPerson() != null) {
            Long personId = dogInputDto.getPerson().getId();
            if (personId == 0) {
                throw new RecordNotFoundException("Person can only be added by ID");
            }
            if(!personRepository.findById(personId).isPresent()) {
                throw new RecordNotFoundException("Person not found");
            }
        }
        Dog dog = transferToDog(dogInputDto);
        dogRepository.save(dog);

        return dog.getId();

    }

    //DELETE deletes an entity from the database if found
    public void deleteDog(Long id) {

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
            long personId = dogInputDto.getPerson().getId();
            if(personId == 0) {
                throw new RecordNotFoundException("Person can only be added by ID");
            }
            Person dogOwner;
            //Check if given Person id exists in database
            try {
                dogOwner = personRepository.findById(personId).get();
            } catch (NoSuchElementException ex) {
                throw new RecordNotFoundException("Person not found");
            }
            Dog updatedDog = transferToDog(dogInputDto);
            updatedDog.setPerson(dogOwner);
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
    public DogOutputDto patchDog(long id, DogPatchDto dogPatchDto) {
        Optional<Dog> dogFound = dogRepository.findById(id);

        if (dogFound.isPresent()) {

            Dog updatedDog = dogRepository.getReferenceById(id);
            if (dogPatchDto.getName() != null) {
                updatedDog.setName(dogPatchDto.getName());
            }
            if (dogPatchDto.getHairColor() != null) {
                updatedDog.setHairColor(dogPatchDto.getHairColor());
            }
            if (dogPatchDto.getFood() != null) {
                updatedDog.setFood(dogPatchDto.getFood());
            }
            if (dogPatchDto.getSex() != null) {
                String newSexString = dogPatchDto.getSex();
                Sex newSex;
                //Check if given value exists in enumeration
                try {
                    newSex = Sex.valueOf(newSexString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Sex not found");
                }
                updatedDog.setSex(newSex);
            }
            if (dogPatchDto.getWeightInGrams() > 0.0) {
                updatedDog.setWeightInGrams(dogPatchDto.getWeightInGrams());
            }
            if (dogPatchDto.getKindOfHair() != null) {
                updatedDog.setKindOfHair(dogPatchDto.getKindOfHair());
            }
            if (dogPatchDto.getDateOfBirth() != null) {
                updatedDog.setDateOfBirth(dogPatchDto.getDateOfBirth());
            }
            if (dogPatchDto.getDateOfDeath() != null) {
                updatedDog.setDateOfDeath(dogPatchDto.getDateOfDeath());
            }
            if (dogPatchDto.getFood() != null) {
                updatedDog.setFood(dogPatchDto.getFood());
            }
            if (dogPatchDto.getBreed() != null) {
                String newBreedString = dogPatchDto.getBreed();
                Breed newBreed;
                //Check if given value exists in enumeration
                try {
                    newBreed = Breed.valueOf(newBreedString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Breed not found");
                }
                updatedDog.setBreed(newBreed);
            }
            if (dogPatchDto.getBreedGroup() != null) {
                String newBreedGroupString = dogPatchDto.getBreedGroup();
                BreedGroup newBreedGroup;
                //Check if given value exists in enumeration
                try {
                    newBreedGroup = BreedGroup.valueOf(newBreedGroupString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Breed group not found");
                }
                updatedDog.setBreedGroup(newBreedGroup);
            }
            if (dogPatchDto.getChipNumber() != null) {
                updatedDog.setChipnumber(dogPatchDto.getChipNumber());
            }
            if (dogPatchDto.getPerson() != null) {
                long personId = dogPatchDto.getPerson().getId();
                if(personId == 0) {
                    throw new RecordNotFoundException("Person can only be added by ID");
                }
                Person dogOwner;
                //Check if given Person id exists in database
                try {
                    dogOwner = personRepository.findById(personId).get();
                } catch (NoSuchElementException ex) {
                    throw new RecordNotFoundException("Person not found");
                }
                updatedDog.setPerson(dogOwner);
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
        dogDto.setPerson(dog.getPerson());

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
        dog.setPerson(dto.getPerson());

        return dog;
    }
}
