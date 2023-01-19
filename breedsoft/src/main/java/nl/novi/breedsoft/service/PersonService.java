package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.personDtos.PersonInputDto;
import nl.novi.breedsoft.dto.personDtos.PersonOutputDto;
import nl.novi.breedsoft.dto.personDtos.PersonPatchDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.exception.ZipCodeFormatException;
import nl.novi.breedsoft.model.management.*;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.repository.WaitingListItemRepository;
import nl.novi.breedsoft.utility.RepositoryUtility;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final DomesticatedDogRepository domesticatedDogRepository;
    private final WaitingListItemRepository waitingListItemRepository;
    private final RepositoryUtility repositoryUtility;
    
    public PersonService(
        PersonRepository personRepository,
        DomesticatedDogRepository domesticatedDogRepository,
        WaitingListItemRepository waitingListItemRepository,
        RepositoryUtility repositoryUtility
    ) {
        this.personRepository = personRepository;
        this.domesticatedDogRepository = domesticatedDogRepository;
        this.waitingListItemRepository = waitingListItemRepository;
        this.repositoryUtility = repositoryUtility;
    }

    /**
     * A method for retrieval of all persons from the database
     * @return a list of all persons in output dto format
     */
    public List<PersonOutputDto> getAllPersons() {
        List<Person> personList = personRepository.findAll();
        return transferPersonListToOutputDtoList(personList);
    }

    /**
     * A method for retrieval of one person from the database by id
     * @param personId ID of the person for which information is requested
     * @return a person in output dto format
     * @throws RecordNotFoundException throws an exception when no person is found by given id
     */
    public PersonOutputDto getPersonById(Long personId) {
        if(personRepository.findById(personId).isPresent()){
            Person person = personRepository.findById(personId).get();
            return transferToOutputDto(person);
        } else {
            throw new RecordNotFoundException("Person not found in database");
        }
    }

    /**
     * A method for retrieval of a list of person(s) from the database by id
     * @param lastName lastname of the person for which information is requested
     * @return a list of person(s) in output dto format
     * @throws RecordNotFoundException throws an exception when no person is found by given lastname
     */
    public List<PersonOutputDto> getPersonByName(String lastName) {
        if (personRepository.findByLastNameContaining(lastName).isEmpty()){
            throw new RecordNotFoundException("No person with this lastname found in database");
        } else {
            List<Person> personList = personRepository.findByLastNameContaining(lastName);
            List<PersonOutputDto> personOutputDtoList = new ArrayList<>();
            for(Person person: personList) {
                personOutputDtoList.add(transferToOutputDto(person));
            }
            return personOutputDtoList;
        }
    }

    /**
     * A method to retrieve a list of persons who are dog breeders (persons who own a breed dog)
     * @return a list of person(s) in output dto format
     * @throws RecordNotFoundException throws an exception when no person is found that owns a breed dog
     */
    public List<PersonOutputDto> getDogBreeders(){
        List<Person> persons = personRepository.findAll();
        List<Person> dogBreeders = new ArrayList<>();

        for(Person person : persons){
            List<DomesticatedDog> ownedDogs = person.getDogs();
            for(DomesticatedDog domesticatedDog : ownedDogs){
                if(domesticatedDog.getDogStatus().toString().equals("breedDog")){
                    dogBreeders.add(person);
                }
            }
        }
        if(dogBreeders.isEmpty()){
            throw new RecordNotFoundException("There are no dog breeders found");
        }
        return transferPersonListToOutputDtoList(dogBreeders);
    }

    /**
     * A method to create a new person in the database
     * @param personInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the ID of the person created in the database
     * @throws RecordNotFoundException throws an exception when there is no domesticated dog found by given id
     */
    public Long createPerson(PersonInputDto personInputDto){
    //Check if a dog is given
        if(personInputDto.getDogs() != null){
            List<DomesticatedDog> givenDogsList = personInputDto.getDogs();
            List<DomesticatedDog> foundDogsList = new ArrayList<>();
            //for each given dog, check if dog exists.
            //if dog exists, add to foundDogsList
            for(DomesticatedDog domesticatedDog : givenDogsList){
                Long dogId = domesticatedDog.getId();
                if(domesticatedDogRepository.findById(dogId).isPresent()) {
                    DomesticatedDog foundDog = repositoryUtility.getCompleteDogId(dogId);
                    if (foundDog != null) {
                        foundDogsList.add(foundDog);
                    }
                }else{
                    throw new RecordNotFoundException("No dog with this id found in database");
                }
            }
            personInputDto.setDogs(foundDogsList);
        }

        Person person = transferToPerson(personInputDto);
        personRepository.save(person);

        return person.getId();
    }

    /**
     * A method (PUT) sends an enclosed entity of a resource to the server.
     * If the entity already exists, the server overrides the existing object,
     * otherwise the server creates a new entity.
     * @param personId ID of the person for which an update is requested
     * @param personInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return a new or updated person in output dto format
     * @throws RecordNotFoundException throws an exception when given domesticated dog is not found by id
     */
       public Object updatePerson(Long personId, PersonInputDto personInputDto) {
        if (personRepository.findById(personId).isPresent()){
            Person person = personRepository.findById(personId).get();
            Person updatedPerson = transferToPerson(personInputDto);
            List<DomesticatedDog> domesticatedDogList = updatedPerson.getDogs();
            List<DomesticatedDog> newDomesticatedDogList = new ArrayList<>();
            if(domesticatedDogList != null) {
                for (DomesticatedDog domesticatedDog : domesticatedDogList) {
                    long foundDogId = domesticatedDog.getId();
                    if(domesticatedDogRepository.findById(foundDogId).isPresent()) {
                        DomesticatedDog dog = domesticatedDogRepository.findById(foundDogId).get();
                        newDomesticatedDogList.add(dog);
                    }else {
                        throw new RecordNotFoundException("Dog " + foundDogId + " not found");
                    }
                }
                updatedPerson.setDogs(newDomesticatedDogList);
            } else {
                List <DomesticatedDog> currentDomesticatedDogs = person.getDogs();
                for(DomesticatedDog dog : currentDomesticatedDogs){
                    dog.setPerson(null);
                }
            }

            //Keeping the former id, as we will update the existing dog
            updatedPerson.setId(person.getId());
            Person savedPerson = personRepository.save(updatedPerson);

            return transferToOutputDto(savedPerson);

        } else {
            Long newPersonId = createPerson(personInputDto);
            Person newPerson = personRepository.getReferenceById(newPersonId);
            return transferToOutputDto(newPerson);
        }
    }

    /**
     * A method (PATCH) will only update an existing object,
     * with the properties mapped in the request body (that are not null).
     * We do NOT update veterinarian appointment and medical data here.
     * @param personId ID of the person for which an update is requested
     * @param personPatchDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return an updated person in output dto format
     * @throws EnumValueNotFoundException throws an exception when the provided enum value is not found in enum
     * @throws ZipCodeFormatException throws an exception when zipcode is not correctly formatted
     * @throws RecordNotFoundException throws an exception when person is not found by id
     */
    public PersonOutputDto patchPerson(Long personId, PersonPatchDto personPatchDto) {
         if (personRepository.findById(personId).isPresent()) {
            Person updatedPerson = personRepository.getReferenceById(personId);
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
                List <DomesticatedDog> dogs = personPatchDto.getDogs();
                List<DomesticatedDog> newDogs = new ArrayList<>();
                for(DomesticatedDog dog : dogs){
                    Long dogId = dog.getId();
                    if(domesticatedDogRepository.findById(dogId).isPresent()) {
                        DomesticatedDog newDog = repositoryUtility.getCompleteDogId(dogId);
                        newDogs.add(newDog);
                    }
                }
                updatedPerson.setDogs(newDogs);

            }

            personRepository.save(updatedPerson);

            return transferToOutputDto(updatedPerson);

        } else {

            throw new RecordNotFoundException("Person is not found.");

        }
    }

    /**
     * A method for deleting a person from the database by id
     * @param personId ID of the person for which deletion is requested
     * @throws RecordNotFoundException throws an exception when the person is not found
     */
    public void deletePerson(Long personId) {

        if (personRepository.findById(personId).isPresent()){
            // If a person is deleted from the database, this person is detached from owned dogs.
            Person personFound = personRepository.getReferenceById(personId);

            if(personFound.getDogs() != null){
                List<DomesticatedDog> givenDogsList = personFound.getDogs();
                //For each given dog, check if dog exists.
                //if dog exists, delete person from dog
                for(DomesticatedDog domesticatedDog : givenDogsList) {
                    domesticatedDog.setPerson(null);
                }
            }
            //delete person from waiting list
            if(personFound.getWaitingListItems() != null){
                List<WaitingListItem> givenWaitingListItemsList = personFound.getWaitingListItems();
                for(WaitingListItem waitingListItem : givenWaitingListItemsList) {
                    waitingListItemRepository.delete(waitingListItem);
                }
            }
            personRepository.deleteById(personId);
        } else {
            throw new  RecordNotFoundException("No person with given ID found.");
        }
    }

    //DTO helper classes
    /**
     * A method to transform a list with persons to a list of persons in output dto format
     * @param personList list of persons to be transformed
     * @return a list of persons in output dto format
     */
    public List<PersonOutputDto> transferPersonListToOutputDtoList(List<Person> personList){
        List<PersonOutputDto> personDtoList = new ArrayList<>();

        for(Person person : personList) {
            PersonOutputDto dto = transferToOutputDto(person);
            personDtoList.add(dto);
        }
        return personDtoList;
    }

    /**
     * A method to transform a person to a person in output dto format
     * @param person person to be transformed
     * @return a person in output dto format
     */
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

    /**
     * A method to transform a person in input dto format to a person format
     * @param personInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return person in person format
     */
    public Person transferToPerson(PersonInputDto personInputDto){
        Person person = new Person();

        person.setFirstName(personInputDto.getFirstName());
        person.setLastName(personInputDto.getLastName());
        person.setSex(Sex.valueOf(personInputDto.getSex()));
        person.setDateOfBirth(personInputDto.getDateOfBirth());
        person.setStreet(personInputDto.getStreet());
        person.setHouseNumber(personInputDto.getHouseNumber());
        person.setHouseNumberExtension(personInputDto.getHouseNumberExtension());
        person.setZipCode(personInputDto.getZipCode());
        person.setCity(personInputDto.getCity());
        person.setCountry(personInputDto.getCountry());
        if(personInputDto.getDogs() != null){
            person.setDogs(personInputDto.getDogs());
        }

        return person;
    }
}
