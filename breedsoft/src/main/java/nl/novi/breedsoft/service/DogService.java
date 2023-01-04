package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.DogInputDto;
import nl.novi.breedsoft.dto.DogOutputDto;
import nl.novi.breedsoft.dto.DogPatchDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.Person;
import nl.novi.breedsoft.model.animal.enumerations.Status;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.model.animal.enumerations.Breed;
import nl.novi.breedsoft.model.animal.enumerations.BreedGroup;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.repository.DogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    //Output Dto is used for representing data from the database to the user
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
        if (dogRepository.findByNameContaining(name).isEmpty()){
            throw new RecordNotFoundException("No dog with this name found in database");
        } else {
            List<Dog> dogList = dogRepository.findByNameContaining(name);
            List<DogOutputDto> dogOutputDtosList = new ArrayList<>();
            for(Dog dog : dogList){
                dogOutputDtosList.add(transferToOutputDto(dog));
            }
            return dogOutputDtosList;
        }
    }

    public List<DogOutputDto> getAllChildren(Long id){
        List<Dog> children = new ArrayList<>();

        if(dogRepository.findById(id).isEmpty()){
            throw new RecordNotFoundException("No dog with this id found in database");
        } else {
            List<Dog> allDogs = dogRepository.findAll();
            for(Dog dog : allDogs){
                if(Objects.equals(dog.getParentId(), id)){
                    children.add(dog);
                }
            }
            if(children.isEmpty()){
                throw new RecordNotFoundException("This dog doesn't have children");
            }
        }

        return transferDogListToDtoList(children);
    }

    //Create creates a new entity in the database
    public Long createDog(DogInputDto dogInputDto){
        //Check if a dog owner is given
        if(dogInputDto.getPerson() != null) {
            Person dogOwner = getCompletePersonById(dogInputDto.getPerson().getId());
            if (dogOwner == null) {
                throw new RecordNotFoundException("Provided dog owner does not exist");
            }
        }
        Dog dog = transferToDog(dogInputDto);
        dogRepository.save(dog);

        return dog.getId();
    }

    public List<DogOutputDto> createLitterList(List<DogInputDto> dogInputDtoList, Long id){
       List<DogOutputDto> createdDogsFromLitterArray = new ArrayList<>();
        if(dogRepository.findById(id).isEmpty()){
            throw new RecordNotFoundException("No dog with this id found in database");
        } else {
            for(DogInputDto child : dogInputDtoList){
                    Dog transferredDog = transferToDog(child);
                    transferredDog.setParentId(id);
                try {
                    dogRepository.save(transferredDog);
                }catch(Exception ex){
                    throw new RecordNotFoundException("Adding dog to database failed");
                }
                    DogOutputDto createdDog = transferToOutputDto(transferredDog);
                    createdDogsFromLitterArray.add(createdDog);
            }
        }
        if (createdDogsFromLitterArray.isEmpty()) {
            throw new RecordNotFoundException("No dogs are created");
        }
        return createdDogsFromLitterArray;
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
            Dog updatedDog = transferToDog(dogInputDto);
            if (dogInputDto.getPerson() != null) {
                Person dogOwner = getCompletePersonById(dogInputDto.getPerson().getId());
                if (dogOwner == null) {
                    throw new RecordNotFoundException("Provided dog owner does not exist");
                }
                updatedDog.setPerson(dogOwner);
            }
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
    public String patchDog(long id, DogPatchDto dogPatchDto) {
        Optional<Dog> dogFound = dogRepository.findById(id);

        if (dogFound.isPresent()) {
            Dog foundDog = dogFound.get();
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
                //Because patchDTO has no checks on enumerations,
                //We have to throw an exception if the value does not exist in the enum
                try {
                    newSex = Sex.valueOf(newSexString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Sex is not found");
                }
                updatedDog.setSex(newSex);
            }
            String message= "";
            if (dogPatchDto.getWeightInGrams() > 0.0) {
                double oldWeight = foundDog.getWeightInGrams();
                updatedDog.setWeightInGrams(dogPatchDto.getWeightInGrams());
                if(dogPatchDto.getWeightInGrams() < oldWeight){
                    message =  "WARNING: Your dog has lost weight!";
                }
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
                //Because patchDTO has no checks on enumerations,
                //We have to throw an exception if the value does not exist in the enum
                try {
                    newBreed = Breed.valueOf(newBreedString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Breed is not found");
                }
                updatedDog.setBreed(newBreed);
            }
            if (dogPatchDto.getBreedGroup() != null) {
                String newBreedGroupString = dogPatchDto.getBreedGroup();
                BreedGroup newBreedGroup;
                //Check if given value exists in enumeration
                //Because patchDTO has no checks on enumerations,
                //We have to throw an exception if the value does not exist in the enum
                try {
                    newBreedGroup = BreedGroup.valueOf(newBreedGroupString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Breedgroup is not found");
                }
                updatedDog.setBreedGroup(newBreedGroup);
            }
            if (dogPatchDto.getChipNumber() != null) {
                updatedDog.setChipNumber(dogPatchDto.getChipNumber());
            }
            if (dogPatchDto.getPerson() != null) {
                Person dogOwner = getCompletePersonById(dogPatchDto.getPerson().getId());
                if (dogOwner == null) {
                    throw new RecordNotFoundException("Provided dog owner does not exist");
                }
                updatedDog.setPerson(dogOwner);
            }
            if (dogPatchDto.getDogStatus() != null) {
                String newStatusString = dogPatchDto.getDogStatus();
                Status newStatus;
                //Check if given value exists in enumeration
                //Because patchDTO has no checks on enumerations,
                //We have to throw an exception if the value does not exist in the enum
                try {
                    newStatus = Status.valueOf(newStatusString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Status is not found");
                }
                updatedDog.setDogStatus(newStatus);
            }
            dogRepository.save(updatedDog);
            return "Your dog has been updated. " + message;

        } else {
            throw new RecordNotFoundException("Dog is not found.");
        }
    }

    public DogOutputDto storeDogImage(Long id, MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            throw new IOException();
        }
        Optional<Dog> optionalDog = dogRepository.findById(id);
        if(optionalDog.isPresent()) {
            Dog dog = optionalDog.get();
            dog.setDogImage(file.getBytes());
            dogRepository.save(dog);
            return (transferToOutputDto(dog));
        } else {
            throw new RecordNotFoundException("Dog is not found.");
        }
    }

    public void deleteDogImage(Long id) {
        Optional<Dog> optionalDog = dogRepository.findById(id);
        if(optionalDog.isPresent()) {
            Dog dog = optionalDog.get();
            dog.setDogImage(null);
            dogRepository.save(dog);
        } else {
            throw new  RecordNotFoundException("No dogs with given ID found.");
        }
    }

    //DTO helper classes

    private List<DogOutputDto> transferDogListToDtoList(List<Dog> dogs){
        List<DogOutputDto> dogDtoList = new ArrayList<>();

        for(Dog dog : dogs) {
            DogOutputDto dto = transferToOutputDto(dog);
            dogDtoList.add(dto);
        }
        return dogDtoList;
    }

    private DogOutputDto transferToOutputDto(Dog dog){

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
        dogDto.setChipNumber(dog.getChipNumber());
        dogDto.setBreed(dog.getBreed());
        dogDto.setBreedGroup(dog.getBreedGroup());
        dogDto.setPerson(dog.getPerson());
        dogDto.setDogImage(dog.getDogImage());
        Long parentId = dog.getParentId();
        if(parentId != null) {
            Optional<Dog> parentDog = dogRepository.findById(parentId);
            if (parentDog.isPresent()) {
                dogDto.setParentId(dog.getParentId());
            } else {
                throw new RecordNotFoundException("Parent ID not found");
            }
        }
        dogDto.setDogStatus(dog.getDogStatus());
        return dogDto;
    }

    private Dog transferToDog(DogInputDto dto){
        Dog dog = new Dog();

        dog.setName(dto.getName());
        dog.setHairColor(dto.getHairColor());
        dog.setFood(dto.getFood());
        if (dto.getSex() != null) {
            String newSexString = dto.getSex();
            Sex newSex;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newSex = Sex.valueOf(newSexString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Sex is not found for dog");
            }
            dog.setSex(newSex);
        }
        dog.setWeightInGrams(dto.getWeightInGrams());
        dog.setKindOfHair(dto.getKindOfHair());
        dog.setDogYears(dto.getDogYears());
        dog.setDateOfBirth(dto.getDateOfBirth());
        dog.setDateOfDeath(dto.getDateOfDeath());
        dog.setChipNumber(dto.getChipNumber());
        dog.setDogYears(dto.getDogYears());
        if (dto.getBreed() != null) {
            String newBreedString = dto.getBreed();
            Breed newBreed;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newBreed = Breed.valueOf(newBreedString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Breed is not found");
            }
            dog.setBreed(newBreed);
        }
        if (dto.getBreedGroup() != null) {
            String newBreedGroupString = dto.getBreedGroup();
            BreedGroup newBreedGroup;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newBreedGroup = BreedGroup.valueOf(newBreedGroupString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Breedgroup is not found");
            }
            dog.setBreedGroup(newBreedGroup);
        }
        dog.setLitter(dto.getLitter());
        dog.setPerson(dto.getPerson());
        Long parentId = dto.getParentId();
        if(parentId != null) {
            Optional<Dog> parentDog = dogRepository.findById(parentId);
            if (parentDog.isPresent()) {
                dog.setParentId(dto.getParentId());
            } else {
                throw new RecordNotFoundException("Parent ID not found");
            }
        }
        if (dto.getDogStatus() != null) {
            String newStatusString = String.valueOf(dto.getDogStatus());
            Status newStatus;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newStatus = Status.valueOf(newStatusString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Status is not found");
            }
            dog.setDogStatus(newStatus);
        }
        return dog;
    }

    /**
     * Get all person information recorded by give ID.
     * @param personId ID of the person information requested
     * @return Person or null if not present.
     */
    private Person getCompletePersonById(Long personId) {
        if (personId == 0) {
            throw new RecordNotFoundException("Missing Person ID");
        }
        Optional<Person> person = personRepository.findById(personId);
        return (person.isPresent()) ? person.get() : null;
    }

}
