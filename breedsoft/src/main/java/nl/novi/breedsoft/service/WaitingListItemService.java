package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemInputDto;
import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemOutputDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.WaitingListItem;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.repository.WaitingListItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WaitingListItemService {

    private final WaitingListItemRepository waitingListItemRepository;
    private final PersonRepository personRepository;

    public WaitingListItemService(WaitingListItemRepository waitingListItemRepository,
                                  PersonRepository personRepository) {
        this.waitingListItemRepository = waitingListItemRepository;
        this.personRepository = personRepository;
    }

    /**
     * A method for retrieval of all users from the database
     * @return a list of all users in output dto format
     */
    public List <WaitingListItemOutputDto> getAllWaitingListItems(){
        List<WaitingListItem> waitingListItemsList = waitingListItemRepository.findAll();
        return transferWaitingListItemListToOutputDtoList(waitingListItemsList);
    }


    /**
     * A method for retrieval of one waiting list item from the database by id
     * @param waitingListItemId ID of the waiting list item for which information is requested
     * @return a waiting list item in output dto format
     * @throws RecordNotFoundException throws an exception when no waiting list item is found by given id
     */
    public List <WaitingListItemOutputDto> getWaitingListItemByPersonID(Long waitingListItemId){
        List <WaitingListItem> waitingListItemList = waitingListItemRepository.findAll();
        List <WaitingListItem> foundWaitingListItem = new ArrayList<>();
        for(WaitingListItem waitingListItem : waitingListItemList){
            Person person = waitingListItem.getPerson();
            if(person.getId().equals(waitingListItemId)){
                foundWaitingListItem = person.getWaitingListItems();
            }
        }
        if(foundWaitingListItem.isEmpty()){
            throw new RecordNotFoundException("No waiting list item found for this person");
        }
        return transferWaitingListItemListToOutputDtoList(foundWaitingListItem);
    }

    /**
     * A method for retrieval of waiting list items for a specific sex
     * @param sex the sex on which a selection is made
     * @return a waiting list item in output dto format
     * @throws RecordNotFoundException throws an exception when no records are found for sex
     * @throws EnumValueNotFoundException throws an exception if sex does not exist in enum
     */
    public List<WaitingListItemOutputDto> getWaitingListItemBySex(String sex){
        Sex newSex;
        try {
            newSex = Sex.valueOf(sex);
        } catch (IllegalArgumentException ex) {
            throw new EnumValueNotFoundException("Sex is not found for dog");
        }
        List<WaitingListItem> foundWaitingListItems = waitingListItemRepository.findBySex(newSex);
        if(foundWaitingListItems.isEmpty()){
            throw new RecordNotFoundException("No waiting list items for this sex.");
        }
        return transferWaitingListItemListToOutputDtoList(foundWaitingListItems);
    }

    /**
     * A method for retrieval of waiting list items for a specific kind of hair
     * @param kindOfHair the kind of hair on which a selection is made
     * @return a waiting list item in output dto format
     * @throws RecordNotFoundException throws an exception when no records are found for the kind of hair
     */
    public List<WaitingListItemOutputDto> getWaitingListItemByKindOfHair(String kindOfHair){
        List<WaitingListItem> foundWaitingListItems = waitingListItemRepository.findByKindOfHairContaining(kindOfHair);
        if(foundWaitingListItems.isEmpty()){
            throw new RecordNotFoundException("No waiting list items for this kind of hair.");
        }
        return transferWaitingListItemListToOutputDtoList(foundWaitingListItems);
    }

    /**
     * A method for retrieval of waiting list items for a specific breed
     * @param breed the breed on which a selection is made
     * @return a waiting list item in output dto format
     * @throws RecordNotFoundException throws an exception when no records are found for breed
     * @throws EnumValueNotFoundException throws an exception if breed does not exist in enum
     */
    public List<WaitingListItemOutputDto> getWaitingListItemByBreed(String breed){
       Breed newBreed;
        try {
            newBreed = Breed.valueOf(breed);
        } catch (IllegalArgumentException ex) {
            throw new EnumValueNotFoundException("Breed is not found for dog");
        }
        List<WaitingListItem> foundWaitingListItems = waitingListItemRepository.findByBreed(newBreed);
        if(foundWaitingListItems.isEmpty()){
            throw new RecordNotFoundException("No waiting list items for this breed.");
        }
        return transferWaitingListItemListToOutputDtoList(foundWaitingListItems);
    }


    /**
     * A method for retrieval of waiting list items for a specific sex, breed and kind of hair
     * @param sex the sex on which a selection is made
     * @param breed the breed on which a selection is made
     * @param kindOfHair the kind of hair on which a selection is made
     * @return a waiting list item in output dto format
     * @throws RecordNotFoundException throws an exception when no records are found for criteria
     * @throws EnumValueNotFoundException throws an exception if sex or breed does not exist in enum
     */
    public List<WaitingListItemOutputDto> getWaitingListItemByCriteria(String sex, String kindOfHair, String breed){
        try {
            Breed.valueOf(breed);
            Sex.valueOf(sex);
        } catch (IllegalArgumentException ex) {
            throw new EnumValueNotFoundException("Breed or Sex is invalid.");
        }
        List<WaitingListItem> foundWaitingListItems = waitingListItemRepository.findByCriteria(sex, kindOfHair, breed);
        if(foundWaitingListItems.isEmpty()){
            throw new RecordNotFoundException("No waiting list items for these criteria.");
        }
        return transferWaitingListItemListToOutputDtoList(foundWaitingListItems);
    }

    /**
     * A method to create a new waiting list item in the database
     * @param waitingListItemInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return the ID of the waiting list item created in the database
     * @throws RecordNotFoundException throws an exception when the provided person does not exist
     */
    public Long createWaitingListItem(WaitingListItemInputDto waitingListItemInputDto){
        //check if person is given
        if(waitingListItemInputDto.getPerson() != null){
            Person person = getCompletePersonById(waitingListItemInputDto.getPerson().getId());
            if(person == null) {
                throw new RecordNotFoundException("Provided person does not exist in the database.");
            }
            waitingListItemInputDto.setPerson(person);
        }
        WaitingListItem waitingListItem = transferToWaitingListItem(waitingListItemInputDto);
        waitingListItemRepository.save(waitingListItem);

        return waitingListItem.getId();
    }

    /**
     * A method for deleting a waiting list item from the database by id
     * @param waitingListItemId ID of the waiting list item for which information is requested
     */
    public void deleteWaitingListItem(Long waitingListItemId){
        if(waitingListItemRepository.findById(waitingListItemId).isPresent()){
            // If a waiting list item is deleted from the database, this waiting list item is detached from persons.
            WaitingListItem waitingListItemFound =waitingListItemRepository.getReferenceById(waitingListItemId);

            if(waitingListItemFound.getPerson() != null){
                waitingListItemFound.setPerson(null);
            }
            waitingListItemRepository.deleteById(waitingListItemId);
        } else {
            throw new RecordNotFoundException("No waiting list item by this id found.");
        }
    }

    //DTO helper classes
    /**
     * A method to transform a waiting list item in input dto format to a waiting list item
     * @param waitingListItemInputDto Data Transfer Objects that carries data between processes in order to reduce the number of methods calls
     * @return waiting list item in waiting list item format
     */
    private WaitingListItem transferToWaitingListItem(WaitingListItemInputDto waitingListItemInputDto){
        WaitingListItem newWaitingListItem = new WaitingListItem();
        newWaitingListItem.setPerson(waitingListItemInputDto.getPerson());
            if (waitingListItemInputDto.getBreed() != null) {
            String newBreedString = waitingListItemInputDto.getBreed();
            Breed newBreed;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newBreed = Breed.valueOf(newBreedString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Breed is not found");
            }
            newWaitingListItem.setBreed(newBreed);
        }
        if (waitingListItemInputDto.getSex() != null) {
            String newSexString = waitingListItemInputDto.getSex();
            Sex newSex;
            //Check if given value exists in enumeration
            //Because patchDTO has no checks on enumerations,
            //We have to throw an exception if the value does not exist in the enum
            try {
                newSex = Sex.valueOf(newSexString);
            } catch (IllegalArgumentException ex) {
                throw new EnumValueNotFoundException("Sex is not found for dog");
            }
            newWaitingListItem.setSex(newSex);
        }
        newWaitingListItem.setKindOfHair(waitingListItemInputDto.getKindOfHair());

        //Set number on waiting list based on highest number
        List<WaitingListItem> waitingListItemList = waitingListItemRepository.findAll();
        int lastNumberOnWaitingList = 0;
        for(WaitingListItem waitingListItem : waitingListItemList){
            int numberOnList = waitingListItem.getNumberOnList();
            if(numberOnList > lastNumberOnWaitingList){
                lastNumberOnWaitingList = numberOnList;
            }
        }
        newWaitingListItem.setNumberOnList(lastNumberOnWaitingList + 1);
        return  newWaitingListItem;
    }

    /**
     * A method to transform a list with waiting list items to a list of waiting list items in output dto format
     * @param waitingListItemList list of waiting list items to be transformed
     * @return a list of waiting list items in output dto format
     */
    public List<WaitingListItemOutputDto> transferWaitingListItemListToOutputDtoList(List<WaitingListItem> waitingListItemList){
        List<WaitingListItemOutputDto> waitingListItemOutputDtoList = new ArrayList<>();

        for(WaitingListItem waitingListItem : waitingListItemList){
            WaitingListItemOutputDto dto = transferWaitingListItemToOutputDto(waitingListItem);
            waitingListItemOutputDtoList.add(dto);
        }
        return waitingListItemOutputDtoList;
    }

    public WaitingListItemOutputDto transferWaitingListItemToOutputDto(WaitingListItem waitingListItem){

        WaitingListItemOutputDto waitingListItemOutputDto = new WaitingListItemOutputDto();
        waitingListItemOutputDto.setId(waitingListItem.getId());
        waitingListItemOutputDto.setNumberOnList(waitingListItem.getNumberOnList());
        waitingListItemOutputDto.setSex(waitingListItem.getSex());
        waitingListItemOutputDto.setBreed(waitingListItem.getBreed());
        waitingListItemOutputDto.setPerson(waitingListItem.getPerson());
        waitingListItemOutputDto.setKindOfHair(waitingListItem.getKindOfHair());

        return waitingListItemOutputDto;
    }

    /**
     * Look for person by id in person repository.
     * When no person ID is given, the get person method returns 0 and an error is thrown.
     * When person ID is found, person is returned.
     * If there is no person found in the repository, null is returned.
     * @param personId ID of the person for which information is requested
     * @return person or null if not present.
     * @throws RecordNotFoundException throws an exception when person ID is missing
     */
       private Person getCompletePersonById(Long personId){
        if(personId == 0){
            throw new RecordNotFoundException("Missing person ID");
        }
        Optional<Person> personOptional = personRepository.findById(personId);
        return personOptional.orElse(null);
    }
}
