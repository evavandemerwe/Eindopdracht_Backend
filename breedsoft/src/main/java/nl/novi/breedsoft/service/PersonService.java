package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.PersonInputDto;
import nl.novi.breedsoft.dto.PersonOutputDto;
import nl.novi.breedsoft.dto.PersonPatchDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.exception.PasswordComplexityException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.exception.ZipCodeFormatException;
import nl.novi.breedsoft.model.animal.Dog;
import nl.novi.breedsoft.model.animal.Person;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.repository.DogRepository;
import nl.novi.breedsoft.repository.PersonRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final DogRepository dogRepository;

    public PersonService(
            PersonRepository personRepository,
            DogRepository dogRepository
    ) {
        this.personRepository = personRepository;
        this.dogRepository = dogRepository;
    }

    //Output Dto is used for representing data from the database to the user
    //Get all persons
    public List<PersonOutputDto> getAllPersons() {
        List<Person> personList = personRepository.findAll();
        return transferPersonListToDtoList(personList);
    }

    //Get one person by ID
    public PersonOutputDto getPersonById(Long id) {
        if (personRepository.findById(id).isPresent()){
            Person person = personRepository.findById(id).get();
            return transferToOutputDto(person);
        } else {
            throw new RecordNotFoundException("Person not found in database");
        }
    }

    //Get person list by Name
    public List<PersonOutputDto> getPersonByName(String lastName) {
        if (personRepository.findByLastNameContaining(lastName).isEmpty()){
            throw new RecordNotFoundException("No person with this name found in database");
        } else {
            List<Person> personList = personRepository.findByLastNameContaining(lastName);
            List<PersonOutputDto> personOutputDtoList = new ArrayList<>();
            for(Person person: personList) {
                personOutputDtoList.add(transferToOutputDto(person));
            }
            return personOutputDtoList;
        }
    }

    //Create creates a new entity in the database
    public Long createPerson(PersonInputDto personInputDto){
    //Check if a dog is given
        if(personInputDto.getDogs() != null){
            List<Dog> givenDogsList = personInputDto.getDogs();
            List<Dog> foundDogsList = new ArrayList<>();
            //for each given dog, check if dog exists.
            //if dog exists, add to foundDogsList
            for(Dog dog : givenDogsList){
                Long dogId = dog.getId();
                Dog foundDog = getCompleteDogId(dogId);
                if(foundDog != null){
                    foundDogsList.add(foundDog);
                }
            }
            personInputDto.setDogs(foundDogsList);
        }

        Person person = transferToPerson(personInputDto);
        personRepository.save(person);

        return person.getId();
    }

    //DELETE deletes an entity from the database if found
    public void deletePerson(Long id) {

        if (personRepository.findById(id).isPresent()){
        // If a person is deleted from the database, this person is detached from owned dogs.
            Person personFound = personRepository.getReferenceById(id);

            if(personFound.getDogs() != null){
                List<Dog> givenDogsList = personFound.getDogs();
                //for each given dog, check if dog exists.
                //if dog exists, add to foundDogsList
                for(Dog dog : givenDogsList) {
                    dog.setPerson(null);
                }
            }
            personRepository.deleteById(id);
        } else {
            throw new  RecordNotFoundException("No person with given ID found.");
        }
    }

    //PUT sends an enclosed entity of a resource to the server.
    //If the entity already exists, the server updates its data. Otherwise, the server creates a new entity
    public Object updatePerson(Long id, PersonInputDto personInputDto) {

        if (personRepository.findById(id).isPresent()){
            Person person = personRepository.findById(id).get();
            Person updatedPerson = transferToPerson(personInputDto);
            List<Dog> dogs = updatedPerson.getDogs();
            for(Dog dog : dogs){
                long foundDogId = dog.getId();
                Optional <Dog> dogFound = dogRepository.findById(foundDogId);
                if(dogFound.isEmpty()) {
                    throw new RecordNotFoundException("Dog " + foundDogId + " not found");
                }
            }
            //Keeping the former id, as we will update the existing dog
            updatedPerson.setId(person.getId());
            personRepository.save(updatedPerson);

            return transferToOutputDto(updatedPerson);

        } else {
            createPerson(personInputDto);
            return personInputDto;
        }

    }
    //PATCH will only update an existing object,
    //with the properties mapped in the request body (that are not null).
    public PersonOutputDto patchPerson(long id, PersonPatchDto personPatchDto) {
        Optional<Person> personFound = personRepository.findById(id);

        if (personFound.isPresent()) {

            Person updatedPerson = personRepository.getReferenceById(id);
            if (personPatchDto.getFirstName() != null) {
                updatedPerson.setFirstName(personPatchDto.getFirstName());
            }
            if (personPatchDto.getLastName() != null) {
                updatedPerson.setLastName(personPatchDto.getLastName());
            }
            if (personPatchDto.getSex() != null) {
                String newSexString = personPatchDto.getSex();
                Sex newSex;
                try{
                    newSex = Sex.valueOf(newSexString);
                } catch (IllegalArgumentException ex) {
                    throw new EnumValueNotFoundException("Sex is not found");
                }
                updatedPerson.setSex(newSex);
            }
            if (personPatchDto.getDateOfBirth() != null) {
                updatedPerson.setDateOfBirth(personPatchDto.getDateOfBirth());
            }
            if (personPatchDto.getStreet() != null) {
                updatedPerson.setStreet(personPatchDto.getStreet());
            }
            if (personPatchDto.getHouseNumber() > 0) {
                updatedPerson.setHouseNumber(personPatchDto.getHouseNumber());
            }
            if (personPatchDto.getHouseNumberExtension() != null) {
                updatedPerson.setHouseNumberExtension(personPatchDto.getHouseNumberExtension());
            }
            if (personPatchDto.getZipCode() != null) {
                String zipcodeRegex = "^[1-9][0-9]{3}?[A-Z]{2}$";
                if (personPatchDto.getZipCode().matches(zipcodeRegex)){
                    updatedPerson.setZipCode(personPatchDto.getZipCode());
                }else{
                    throw new ZipCodeFormatException("Format zipcode as: 1111AA");
                }
                updatedPerson.setZipCode(personPatchDto.getZipCode());
            }
            if (personPatchDto.getCity() != null) {
                updatedPerson.setCity(personPatchDto.getCity());
            }
            if (personPatchDto.getCountry() != null) {
                updatedPerson.setCountry(personPatchDto.getCountry());
            }
            if (personPatchDto.getDogs() != null){
                updatedPerson.setDogs(personPatchDto.getDogs());
            }

            personRepository.save(updatedPerson);

            return transferToOutputDto(updatedPerson);

        } else {

            throw new RecordNotFoundException("Person is not found.");

        }
    }

    //DTO helper classes
    public List<PersonOutputDto> transferPersonListToDtoList(List<Person> persons){
        List<PersonOutputDto> personDtoList = new ArrayList<>();

        for(Person person : persons) {
            PersonOutputDto dto = transferToOutputDto(person);
            personDtoList.add(dto);
        }
        return personDtoList;
    }

    public PersonOutputDto transferToOutputDto(Person person){

        PersonOutputDto personOutputDto = new PersonOutputDto();

        personOutputDto.setId(person.getId());
        personOutputDto.setFirstName(person.getFirstName());
        personOutputDto.setLastName(person.getLastName());
        personOutputDto.setAge(person.getAge());
        personOutputDto.setSex(person.getSex());
        personOutputDto.setStreet(person.getStreet());
        personOutputDto.setHouseNumber(person.getHouseNumber());
        personOutputDto.setHouseNumberExtension(person.getHouseNumberExtension());
        personOutputDto.setZipCode(person.getZipCode());
        personOutputDto.setCity(person.getCity());
        personOutputDto.setCountry(person.getCountry());
        personOutputDto.setDogs(person.getDogs());

        return personOutputDto;
    }

    public Person transferToPerson(PersonInputDto dto){
        Person person = new Person();

        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setSex(Sex.valueOf(dto.getSex()));
        person.setDateOfBirth(dto.getDateOfBirth());
        person.setStreet(dto.getStreet());
        person.setHouseNumber(dto.getHouseNumber());
        person.setHouseNumberExtension(dto.getHouseNumberExtension());
        person.setZipCode(dto.getZipCode());
        person.setCity(dto.getCity());
        person.setCountry(dto.getCountry());
        if(dto.getDogs() != null){
            person.setDogs(dto.getDogs());
        }

        return person;
    }

    private Dog getCompleteDogId(Long dogId){
        if(dogId == 0){
            throw new RecordNotFoundException("Missing Dog ID");
        }
        Optional<Dog> dog = dogRepository.findById(dogId);
        return (dog.isPresent()) ? dog.get() : null;
    }
}
