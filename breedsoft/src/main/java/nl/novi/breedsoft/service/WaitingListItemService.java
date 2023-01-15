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

    //GET a list of all waiting list items from the database
    public List <WaitingListItemOutputDto> getAllWaitingListItems(){
        List<WaitingListItem> waitingListItemsList = waitingListItemRepository.findAll();
        return transferWaitingListItemListToDtoList(waitingListItemsList);
    }

    //GET a list of all waiting list items by person ID
    public List <WaitingListItemOutputDto> getWaitingListItemByPersonID(Long id){
        List <WaitingListItem> waitingListItemList = waitingListItemRepository.findAll();
        List <WaitingListItem> foundWaitingListItem = new ArrayList<>();
        for(WaitingListItem waitingListItem : waitingListItemList){
            Person person = waitingListItem.getPerson();
            if(person.getId().equals(id)){
                foundWaitingListItem = person.getWaitingListItems();
            }
        }
        if(foundWaitingListItem.isEmpty()){
            throw new RecordNotFoundException("No waiting list item found for this person");
        }
        return transferWaitingListItemListToDtoList(foundWaitingListItem);
    }

    //GET a list of all waiting list items by sex
    public List<WaitingListItemOutputDto> getWaitingListItemBySex(String sex){
        Sex newSex;
        try {
            newSex = Sex.valueOf(sex.toLowerCase());
        } catch (IllegalArgumentException ex) {
            throw new EnumValueNotFoundException("Sex is not found for dog");
        }
        List<WaitingListItem> allWaitingListItems = waitingListItemRepository.findAll();
        List<WaitingListItem> foundWaitingListItems = new ArrayList<>();

        for(WaitingListItem waitingListItem : allWaitingListItems){
            Sex foundSex = waitingListItem.getSex();
            if(foundSex.equals(newSex)){
                foundWaitingListItems.add(waitingListItem);
            }
        }

        if(foundWaitingListItems.isEmpty()){
            throw new RecordNotFoundException("No waiting list items for this sex.");
        }
        return transferWaitingListItemListToDtoList(foundWaitingListItems);
    }

    //GET a list of all waiting list items by breed
    public List<WaitingListItemOutputDto> getWaitingListItemByBreed(String breed){
        Breed newBreed;
        String capitalFirstLetter = breed.substring(0, 1).toUpperCase();
        String breedCapitalized = capitalFirstLetter + breed.substring(1);
        try {
            newBreed = Breed.valueOf(breedCapitalized);
        } catch (IllegalArgumentException ex) {
            throw new EnumValueNotFoundException("Breed is not found for dog");
        }
        List<WaitingListItem> allWaitingListItems = waitingListItemRepository.findAll();
        List<WaitingListItem> foundWaitingListItems = new ArrayList<>();

        for(WaitingListItem waitingListItem : allWaitingListItems){
            Breed foundBreed = waitingListItem.getBreed();
            if(foundBreed.equals(newBreed)){
                foundWaitingListItems.add(waitingListItem);
            }
        }
        if(foundWaitingListItems.isEmpty()){
            throw new RecordNotFoundException("No waiting list items for this breed.");
        }
        return transferWaitingListItemListToDtoList(foundWaitingListItems);
    }

    //GET a list of all waiting list items by kindOfHair
    public List<WaitingListItemOutputDto> getWaitingListItemByKindOfHair(String kindOfHair){
        List<WaitingListItem> foundWaitingListItems = waitingListItemRepository.findByKindOfHairContaining(kindOfHair);
        if(foundWaitingListItems.isEmpty()){
            throw new RecordNotFoundException("No waiting list items for this kind of hair.");
        }
        return transferWaitingListItemListToDtoList(foundWaitingListItems);
    }

    //Create a new waiting list item
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

    public void deleteAppointment(Long id){
        if(waitingListItemRepository.findById(id).isPresent()){
            // If a waiting list item is deleted from the database, this waiting list item is detached from persons.
            WaitingListItem waitingListItemFound =waitingListItemRepository.getReferenceById(id);

            if(waitingListItemFound.getPerson() != null){
                waitingListItemFound.setPerson(null);
            }
            waitingListItemRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("No waiting list item by this id found.");
        }
    }

    //DTO helper classes
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

    public List<WaitingListItemOutputDto> transferWaitingListItemListToDtoList(List<WaitingListItem> waitingListItems){
        List<WaitingListItemOutputDto> waitingListItemOutputDtoList = new ArrayList<>();

        for(WaitingListItem waitingListItem : waitingListItems){
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

    //Look for person by id in personrespository.
    //When nu person ID is given, the get person method returns 0 and an error is thrown.
    //When person id is found, person is returned. If there is no person found in the repository, null is returned.
    private Person getCompletePersonById(Long personId){
        if(personId == 0){
            throw new RecordNotFoundException("Missing person ID");
        }
        Optional<Person> personOptional = personRepository.findById(personId);
        return personOptional.orElse(null);
    }
}
