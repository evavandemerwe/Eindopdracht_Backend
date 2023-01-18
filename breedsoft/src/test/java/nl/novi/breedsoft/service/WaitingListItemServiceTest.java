package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemOutputDto;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.WaitingListItem;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.repository.WaitingListItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class WaitingListItemServiceTest {

    @Mock
    WaitingListItemRepository waitingListItemRepository;

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    WaitingListItemService waitingListItemService;

    @Captor
    ArgumentCaptor<WaitingListItem> waitingListItem;

    @BeforeEach
    void setUp(){

         }

    @AfterEach
    void tear(){
        waitingListItem = null;
    }

    @Test
    void getAllWaitingListItems() {
        //Arrange
        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");

        List<WaitingListItem> waitingListItemList = new ArrayList<>();
        WaitingListItem waitingListItem = new WaitingListItem();
        waitingListItem.setId(1L);
        waitingListItem.setNumberOnList(1);
        waitingListItem.setSex(Sex.female);
        waitingListItem.setBreed(Breed.Dachschund);
        waitingListItem.setPerson(person);
        waitingListItem.setKindOfHair("Long Haired");

        waitingListItemList.add(waitingListItem);
        //Act
        when(waitingListItemRepository.findById(1L)).thenReturn(Optional.of(waitingListItem));
        when(waitingListItemRepository.findAll()).thenReturn(waitingListItemList);

        List<WaitingListItem> waitingListItemOutputDtoList = waitingListItemRepository.findAll();

        //Assert
        assertEquals(waitingListItemList, waitingListItemOutputDtoList);
    }

    @Test
    void getWaitingListItemByPersonID() {
    }

    @Test
    void getWaitingListItemBySex() {
    }

    @Test
    void getWaitingListItemByKindOfHair() {
    }

    @Test
    void getWaitingListItemByBreed() {
    }

    @Test
    void getWaitingListItemByCriteria() {
    }

    @Test
    void createWaitingListItem() {
    }

    @Test
    void deleteAppointment() {
    }

    @Test
    void transferWaitingListItemListToDtoList() {
    }

    @Test
    void transferWaitingListItemToOutputDto() {
    }
}