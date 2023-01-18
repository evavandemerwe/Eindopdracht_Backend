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
        Person eva = new Person();
        eva.setId(1L);
        eva.setFirstName("Eva");
        eva.setLastName("Hauber");
        eva.setStreet("Maas");
        eva.setHouseNumber(31);
        eva.setZipCode("5172CN");
        eva.setCity("Kaatsheuvel");

        Person teun = new Person();
        teun.setId(2L);
        teun.setFirstName("Teun");
        teun.setLastName("van de Merwe");
        teun.setStreet("Maas");
        teun.setHouseNumber(31);
        teun.setZipCode("5172CN");
        teun.setCity("Kaatsheuvel");

        WaitingListItem waitingListItem1 = new WaitingListItem();
        waitingListItem1.setId(1L);
        waitingListItem1.setNumberOnList(1);
        waitingListItem1.setSex(Sex.female);
        waitingListItem1.setBreed(Breed.Dachschund);
        waitingListItem1.setPerson(eva);
        waitingListItem1.setKindOfHair("Long Haired");
        waitingListItemList.add(waitingListItem1);

        List<WaitingListItem> waitingListEva= new ArrayList<>();
        waitingListEva.add(waitingListItem1);
        eva.setWaitingListItems(waitingListEva);

        WaitingListItem waitingListItem2 = new WaitingListItem();
        waitingListItem2.setId(2L);
        waitingListItem2.setNumberOnList(1);
        waitingListItem2.setSex(Sex.female);
        waitingListItem2.setBreed(Breed.Dachschund);
        waitingListItem2.setPerson(teun);
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
        assertEquals("Eva", result.get(0).getPerson().getFirstName());
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