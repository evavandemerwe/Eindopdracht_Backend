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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaitingListItemServiceTest {

    @Mock
    WaitingListItemRepository waitingListItemRepository;

    @Mock
    PersonRepository personRepository;

    @Mock
    WaitingListItemService waitingListItemService;

    List<WaitingListItem> waitingListItemList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        this.waitingListItemService =
                new WaitingListItemService(
                        this.waitingListItemRepository,
                        this.personRepository
                );
        Person person1 = new Person();
        person1.setId(1L);
        person1.setFirstName("Eva");
        person1.setLastName("Hauber");
        person1.setStreet("Maas");
        person1.setHouseNumber(31);
        person1.setZipCode("5172CN");
        person1.setCity("Kaatsheuvel");


        Person person2 = new Person();
        person1.setId(2L);
        person1.setFirstName("Teun");
        person1.setLastName("van de Merwe");
        person1.setStreet("Maas");
        person1.setHouseNumber(31);
        person1.setZipCode("5172CN");
        person1.setCity("Kaatsheuvel");

        WaitingListItem waitingListItem1 = new WaitingListItem();
        waitingListItem1.setId(1L);
        waitingListItem1.setNumberOnList(1);
        waitingListItem1.setSex(Sex.female);
        waitingListItem1.setBreed(Breed.Dachschund);
        waitingListItem1.setPerson(person1);
        waitingListItem1.setKindOfHair("Long Haired");
        waitingListItemList.add(waitingListItem1);
        List<WaitingListItem> waitingListPerson1= new ArrayList();
        waitingListPerson1.add(waitingListItem1);

        person1.setWaitingListItems(waitingListPerson1);

        WaitingListItem waitingListItem2 = new WaitingListItem();
        waitingListItem2.setId(2L);
        waitingListItem2.setNumberOnList(1);
        waitingListItem2.setSex(Sex.female);
        waitingListItem2.setBreed(Breed.Dachschund);
        waitingListItem2.setPerson(person2);
        waitingListItem2.setKindOfHair("Long Haired");

        waitingListItemList.add(waitingListItem2);
    }

    @AfterEach
    void tear(){
        waitingListItemService = null;
        waitingListItemList = null;
        waitingListItemRepository = null;
    }

    @Test
    void getAllWaitingListItems() {
        //Arrange
        when(waitingListItemRepository.findAll()).thenReturn(waitingListItemList);

        //Act
        List<WaitingListItemOutputDto> result = waitingListItemService.getAllWaitingListItems();

        //Assert
        assertEquals(Sex.female, result.get(0).getSex());
        assertEquals("Teun", result.get(0).getPerson().getFirstName());
    }
    @Test
    void getAllWaitingListItemsWhenListIsEmpty() {
        //Arrange
        when(waitingListItemRepository.findAll()).thenReturn(new ArrayList<>());

        //Act
        List<WaitingListItemOutputDto> result = waitingListItemService.getAllWaitingListItems();

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getWaitingListItemByPersonID() {
        //Arrange
        when(waitingListItemRepository.findAll()).thenReturn(waitingListItemList);
        //Act
        List<WaitingListItemOutputDto> result = waitingListItemService.getWaitingListItemByPersonID(1L);
        //Assert
        assertEquals(1, result.size());
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