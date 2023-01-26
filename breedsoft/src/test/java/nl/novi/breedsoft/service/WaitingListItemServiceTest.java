package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemOutputDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.WaitingListItem;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.repository.WaitingListItemRepository;
import nl.novi.breedsoft.utility.RepositoryUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WaitingListItemServiceTest {

    @Mock
    WaitingListItemRepository waitingListItemRepository;

    @Mock
    RepositoryUtility repositoryUtility;

    WaitingListItemService waitingListItemService;

    List<WaitingListItem> waitingListItemList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        this.waitingListItemService =
                new WaitingListItemService(
                        this.waitingListItemRepository,
                        this.repositoryUtility
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
        waitingListItem1.setKindOfHair("Long haired");
        waitingListItemList.add(waitingListItem1);

        List<WaitingListItem> waitingListEva= new ArrayList<>();


        WaitingListItem waitingListItem2 = new WaitingListItem();
        waitingListItem2.setId(2L);
        waitingListItem2.setNumberOnList(2);
        waitingListItem2.setSex(Sex.female);
        waitingListItem2.setBreed(Breed.Dachschund);
        waitingListItem2.setPerson(eva);
        waitingListItem2.setKindOfHair("Long haired");

        waitingListEva.add(waitingListItem1);
        waitingListEva.add(waitingListItem2);
        eva.setWaitingListItems(waitingListEva);

        waitingListItemList.add(waitingListItem2);

        WaitingListItem waitingListItem3 = new WaitingListItem();
        waitingListItem3.setId(3L);
        waitingListItem3.setNumberOnList(3);
        waitingListItem3.setSex(Sex.male);
        waitingListItem3.setBreed(Breed.Dachschund);
        waitingListItem3.setPerson(teun);
        waitingListItem3.setKindOfHair("Long haired");

        waitingListItemList.add(waitingListItem3);
    }

    @AfterEach
    void tear(){
        waitingListItemService = null;
        waitingListItemList = null;
        waitingListItemRepository = null;
        repositoryUtility = null;
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
        assertEquals(2, result.size());
    }

    @Test
    void getWaitingListItemByPersonIDWhenListIsEmpty() {
        //Arrange
        when(waitingListItemRepository.findAll()).thenReturn(new ArrayList<>());
        //Act
        RecordNotFoundException thrown = assertThrows(
                RecordNotFoundException.class,
                () -> waitingListItemService.getWaitingListItemByPersonID(1L),
                "Expected enum value not found exception to be thrown, but it didn't");
        //Assert
        assertTrue(thrown.getMessage().contentEquals("No waiting list item found for this person"));
    }

    @Test
    void getWaitingListItemBySex() {
        //Arrange
        when(waitingListItemRepository.findBySex(Sex.valueOf("female"))).thenReturn(waitingListItemList);
        //Act
        List<WaitingListItemOutputDto> result = waitingListItemService.getWaitingListItemBySex("female");
        //Assert
        assertEquals(Sex.female, result.get(0).getSex());
    }

    @Test
    void getWaitingListItemBySexWhenSexInvalid() {
        //Arrange
        //Act
        EnumValueNotFoundException thrown = assertThrows(
                EnumValueNotFoundException.class,
                () -> waitingListItemService.getWaitingListItemBySex("x"),
                "Expected enum value not found exception to be thrown, but it didn't");
        //Assert
        assertTrue(thrown.getMessage().contentEquals("Sex is not found"));
    }

    @Test
    void getWaitingListItemBySexWhenEmptyList() {
        //Arrange
        when(waitingListItemRepository.findBySex(Sex.valueOf("female"))).thenReturn(new ArrayList<>());
        //Act
        RecordNotFoundException thrown = assertThrows(
                RecordNotFoundException.class,
                () -> waitingListItemService.getWaitingListItemBySex("female"),
                "Expected RecordNotFoundException to be thrown, but it didn't");
        //Assert
        assertTrue(thrown.getMessage().contentEquals("No waiting list items for this sex."));
    }

    @Test
    void getWaitingListItemByKindOfHair() {
        //Arrange
        when(waitingListItemRepository.findByKindOfHairContaining("Long haired")).thenReturn(waitingListItemList);
        //Act
        List<WaitingListItemOutputDto> result = waitingListItemService.getWaitingListItemByKindOfHair("Long haired");
        //Assert
        assertEquals(3, result.size());
        assertEquals("Long haired", result.get(0).getKindOfHair());
    }
    @Test
    void getWaitingListItemByKindOfHairWhenInvalidHair() {
        //Arrange
        //Act
        RecordNotFoundException thrown = assertThrows(
                RecordNotFoundException.class,
                () -> waitingListItemService.getWaitingListItemByKindOfHair("x"),
                "Expected RecordNotFoundException to be thrown, but it didn't");
        //Assert
        assertTrue(thrown.getMessage().contentEquals("No waiting list items for this kind of hair."));
    }
    @Test
    void getWaitingListItemByBreed() {
        //Arrange
        when(waitingListItemRepository.findByBreed(Breed.Dachschund)).thenReturn(waitingListItemList);
        //Act
        List<WaitingListItemOutputDto> result = waitingListItemService.getWaitingListItemByBreed("Dachschund");
        //Assert
        assertEquals(Breed.Dachschund, result.get(0).getBreed());
    }

    @Test
    void getWaitingListItemBySexWhenBreedInvalid() {
        //Arrange
        //Act
        EnumValueNotFoundException thrown = assertThrows(
                EnumValueNotFoundException.class,
                () -> waitingListItemService.getWaitingListItemByBreed("x"),
                "Expected enum value not found exception to be thrown, but it didn't");
        //Assert
        assertTrue(thrown.getMessage().contentEquals("Breed is not found for dog"));
    }

    @Test
    void getWaitingListItemByBreedWhenEmptyList() {
        //Arrange
        when(waitingListItemRepository.findByBreed(Breed.Dachschund)).thenReturn(new ArrayList<>());
        //Act
        RecordNotFoundException thrown = assertThrows(
                RecordNotFoundException.class,
                () -> waitingListItemService.getWaitingListItemByBreed("Dachschund"),
                "Expected RecordNotFoundException to be thrown, but it didn't");
        //Assert
        assertTrue(thrown.getMessage().contentEquals("No waiting list items for this breed."));
    }

    @Test
    void getWaitingListItemByCriteria() {
        //Arrange
        when(waitingListItemRepository.findByCriteria("female", "Long Haired", "Dachschund"))
                .thenReturn(waitingListItemList.subList(0,2));
        //Act
        List<WaitingListItemOutputDto> result = waitingListItemService.getWaitingListItemByCriteria
                ("female", "Long Haired", "Dachschund");
        //Assert
        assertEquals(Sex.female, result.get(0).getSex());
        assertEquals("Long haired", result.get(0).getKindOfHair());
        assertEquals(Breed.Dachschund, result.get(0).getBreed());
    }

    @Test
    void getWaitingListItemByCriteriaWhenBreedIsInvalid(){
        //Arrange
        //Act
        EnumValueNotFoundException thrown = assertThrows(
                EnumValueNotFoundException.class,
                () -> waitingListItemService.getWaitingListItemByCriteria
                        ("female", "Long Haired", "x"),
                "Expected enum value not found exception to be thrown, but it didn't");
        //Assert
        assertTrue(thrown.getMessage().contentEquals("Breed or Sex is invalid."));

    }
    @Test
    void getWaitingListItemByCriteriaWhenSexIsInvalid(){
        //Arrange
        //Act
        EnumValueNotFoundException thrown = assertThrows(
                EnumValueNotFoundException.class,
                () -> waitingListItemService.getWaitingListItemByCriteria
                        ("x", "Long Haired", "Dachschund"),
                "Expected enum value not found exception to be thrown, but it didn't");
        //Assert
        assertTrue(thrown.getMessage().contentEquals("Breed or Sex is invalid."));

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