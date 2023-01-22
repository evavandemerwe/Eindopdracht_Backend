package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemInputDto;
import nl.novi.breedsoft.dto.waitingListItemDtos.WaitingListItemOutputDto;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.Status;
import nl.novi.breedsoft.service.WaitingListItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebMvcTest(WaitingListItemController.class)
class WaitingListItemControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WaitingListItemService waitingListItemService;

    WaitingListItemOutputDto waitingListItemOutputDto = new WaitingListItemOutputDto();
    WaitingListItemInputDto waitingListItemInputDto = new WaitingListItemInputDto();

    List<WaitingListItemOutputDto> waitingListItemOutputDtoList = new ArrayList();

    DomesticatedDog dog = new DomesticatedDog();
    List<DomesticatedDog> dogs = new ArrayList();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        dog.setName("Saar");
        dog.setDogStatus(Status.breedDog);
        dog.setBreed(Breed.Dachschund);
        dogs.add(dog);

        Person person = new Person();
        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        person.setDogs(dogs);

        waitingListItemInputDto.setPerson(person);
        waitingListItemInputDto.setSex("female");
        waitingListItemInputDto.setBreed("Dachschund");
        waitingListItemInputDto.setKindOfHair("Long haired");

        waitingListItemOutputDto.setPerson(person);
        waitingListItemOutputDto.setSex(Sex.female);
        waitingListItemOutputDto.setBreed(Breed.Dachschund);
        waitingListItemOutputDto.setKindOfHair("Long haired");
        waitingListItemOutputDtoList.add(waitingListItemOutputDto);
    }
    @AfterEach
    void tear() {
        context = null;
        mockMvc = null;
        waitingListItemService = null;
        waitingListItemOutputDto = null;
        waitingListItemInputDto = null;
        waitingListItemOutputDtoList = null;
        dog = null;
        dogs = null;
    }

    @Test
    void getAllWaitingListItems() throws Exception {
        when(waitingListItemService.getAllWaitingListItems())
                .thenReturn(waitingListItemOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].kindOfHair", is("Long haired")));
    }

    @Test
    void getAllWaitingListItemsForPersonId() throws Exception {
        when(waitingListItemService.getWaitingListItemByPersonID(1L))
                .thenReturn(waitingListItemOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems/personid/{id}", "1")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].kindOfHair", is("Long haired")));
    }

    @Test
    void getAllWaitingListItemsPersonIdEmptyList() throws Exception {
        when(waitingListItemService.getWaitingListItemByPersonID(2L))
                .thenThrow(RecordNotFoundException.class);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems/personid/{id}", "2")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllWaitingListItemsForSex() throws Exception {
        when(waitingListItemService.getWaitingListItemBySex("female"))
                .thenReturn(waitingListItemOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems/sex/{sex}", "female")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].sex", is("female")));
    }

    @Test
    void getAllWaitingListItemsForSexEmptyList() throws Exception {
        when(waitingListItemService.getWaitingListItemBySex("male"))
                .thenThrow(RecordNotFoundException.class);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems/sex/{sex}", "male")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllWaitingListItemsForBreed() throws Exception {
        when(waitingListItemService.getWaitingListItemByBreed("Dachschund"))
                .thenReturn(waitingListItemOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems/breed/{breed}", "Dachschund")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].breed", is("Dachschund")));
    }

    @Test
    void getAllWaitingListItemsForBreedWithEmptyList() throws Exception {
        when(waitingListItemService.getWaitingListItemByBreed("Pinscher"))
                .thenThrow(RecordNotFoundException.class);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems/breed/{breed}", "Pinscher")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllWaitingListItemsForKindOfHair() throws Exception {
            when(waitingListItemService.getWaitingListItemByKindOfHair("Long haired"))
                    .thenReturn(waitingListItemOutputDtoList);
            this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/waitinglistitems//kindofhair/{kindofhair}", "Long haired")
                            .with(jwt()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.
                            jsonPath("$.[0].kindOfHair", is("Long haired")));
    }

    @Test
    void getAllWaitingListItemsForKindOfHairWithEmptyList() throws Exception {
        when(waitingListItemService.getWaitingListItemByKindOfHair("Long haired"))
                .thenThrow(RecordNotFoundException.class);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems/kindOfHair/{breed}", "Short haired")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllWaitingListItemsForCriteria() throws Exception {
        String sex = "female";
        String kindOfHair = "Short haired";
        String breed = "Pinscher";
        when(waitingListItemService.getWaitingListItemByCriteria(sex, kindOfHair, breed))
                .thenReturn(waitingListItemOutputDtoList);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(
                                "/waitinglistitems/criteria?sex={sex}&kindofhair={kindOfHair}&breed={breed}",
                                        sex, kindOfHair, breed)
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void createWaitingListItem() throws Exception {
        when(waitingListItemService.createWaitingListItem(waitingListItemInputDto))
                .thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/waitinglistitems")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(" {\n" +
                                        "        \"person\": {\n" +
                                        "            \"id\": 2002\n" +
                                        "        },\n" +
                                        "        \"breed\": \"Dachschund\",\n" +
                                        "        \"sex\": \"female\",\n" +
                                        "        \"kindOfHair\": \"Long haired\"\n" +
                                        "    }")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    void createWaitingListItemWithBindingResultError() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/waitinglistitems")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteWaitingListItem() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/waitinglistitems/{id}", "1")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWaitingListItemWithInvalidId() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/waitinglistitems/{id}", "")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }
}