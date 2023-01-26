package nl.novi.breedsoft.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.breedsoft.cleanupH2DatabaseTestListener.CleanupH2DatabaseTestListener;
import nl.novi.breedsoft.dto.personDtos.PersonInputDto;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.WaitingListItem;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.BreedGroup;
import nl.novi.breedsoft.model.management.enumerations.Status;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.repository.WaitingListItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, CleanupH2DatabaseTestListener.class})
public class PersonIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    DomesticatedDogRepository domesticatedDogRepository;

    @Autowired
    WaitingListItemRepository waitingListItemRepository;

    Person person = new Person();
    PersonInputDto personInputDto = new PersonInputDto();

    DomesticatedDog domesticatedDog = new DomesticatedDog();
    List<DomesticatedDog> dogs = new ArrayList<>();

    WaitingListItem waitingListItem = new WaitingListItem();
    List<WaitingListItem> waitingListItemList = new ArrayList<>();

    @Test
    void getAllPersons() throws  Exception {
        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.ownedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/persons").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(person.getId()))
                .andExpect(jsonPath("$[0].firstName").value("Eva"))
                .andExpect(jsonPath("$[0].dogs.[0].id").value(domesticatedDog.getId()))
                .andExpect(jsonPath("$[0].dogs.[0].name").value("Saar"));
    }

    @Test
    void getPersonById() throws  Exception {

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.ownedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/persons/id/{id}", person.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.firstName").value("Eva"))
                .andExpect(jsonPath("$.dogs.[0].id").value(domesticatedDog.getId()))
                .andExpect(jsonPath("$.dogs.[0].name").value("Saar"));
    }

    @Test
    void getPersonWithUnknownId() throws  Exception {
        mockMvc.perform(get("/persons/id/{id}", "222").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Person not found in database"));
    }

    @Test
    void getPersonByName() throws  Exception {

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.ownedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/persons/name/{name}", "Hauber").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(person.getId()))
                .andExpect(jsonPath("$[0].firstName").value("Eva"))
                .andExpect(jsonPath("$[0].dogs.[0].id").value(domesticatedDog.getId()))
                .andExpect(jsonPath("$[0].dogs.[0].name").value("Saar"));
    }

    @Test
    void getPersonWithUnknownName() throws  Exception {
        mockMvc.perform(get("/persons/name/{name}", "Dikbips").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No person with this lastname found in database"));
    }

    @Test
    void getDogBreeders() throws Exception{
        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/persons/dogbreeders").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(person.getId()))
                .andExpect(jsonPath("$[0].firstName").value("Eva"))
                .andExpect(jsonPath("$[0].lastName").value("Hauber"))
                .andExpect(jsonPath("$[0].sex").value("female"))
                .andExpect(jsonPath("$[0].street").value("Maas"))
                .andExpect(jsonPath("$[0].houseNumber").value("31"))
                .andExpect(jsonPath("$[0].city").value("Kaatsheuvel"));
    }

    @Test
    void getDogBreedersWhenNoBreedDogsExist() throws Exception{
        mockMvc.perform(get("/persons/dogbreeders").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("There are no dog breeders found"));
    }

    @Test
    void createPerson() throws Exception{

        personInputDto = new PersonInputDto();
        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 2));

        mockMvc.perform(post("/persons").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personInputDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Person is successfully created!"));
    }

    @Test
    void createPersonWithInvalidZipCode() throws Exception{

        personInputDto = new PersonInputDto();
        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("517CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 2));

        mockMvc.perform(post("/persons").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personInputDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("zipCode: Format zipcode as: 1111AA")));
    }

    @Test
    void createPersonWithMissingValues() throws Exception{

        personInputDto = new PersonInputDto();
        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");

        mockMvc.perform(post("/persons").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personInputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPersonWithUnknownDog() throws Exception {

        domesticatedDog.setId(1L);

        List<DomesticatedDog> dogs = new ArrayList<>();
        dogs.add(domesticatedDog);

        personInputDto = new PersonInputDto();
        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 02));

        personInputDto.setDogs(dogs);

        mockMvc.perform(post("/persons").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(personInputDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No dog with this id found in database"));
    }

    @Test
    void createPersonWithDog() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);
        dogs.add(domesticatedDog);

        personInputDto = new PersonInputDto();
        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 02));

        personInputDto.setDogs(dogs);

        mockMvc.perform(post("/persons").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personInputDto)))
                .andExpect(content().string("Person is successfully created!"));
    }

    @Test
    void updatePerson() throws Exception {

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        personInputDto = new PersonInputDto();
        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 02));

        mockMvc.perform(put("/persons/{id}", person.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personInputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.age").value(37));
    }

    @Test
    void updatePersonWithUnknownPerson() throws Exception {

        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 02));

        mockMvc.perform(put("/persons/{id}", "111").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personInputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Eva"))
                .andExpect(jsonPath("$.lastName").value("Hauber"))
                .andExpect(jsonPath("$.country").value("Nederland"));
    }

    @Test
    void patchPerson() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);
        dogs.add(domesticatedDog);

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        personInputDto = new PersonInputDto();
        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setHouseNumberExtension("A");
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDogs(dogs);
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 02));

        mockMvc.perform(patch("/persons/{id}", person.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personInputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(person.getId()))
                .andExpect(jsonPath("$.age").value(37));
    }

    @Test
    void patchPersonWithUnknownPerson() throws Exception {

        personInputDto = new PersonInputDto();
        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 02));

        mockMvc.perform(patch("/persons/{id}", "111").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personInputDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchPersonInvalidZipCode() throws Exception {

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("517CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setCountry("Nederland");
        personInputDto.setDateOfBirth(LocalDate.of(1986, Month.JANUARY, 02));

        mockMvc.perform(patch("/persons/{id}", person.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(personInputDto)))
                .andExpect(status().isPreconditionFailed())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Format zipcode as: 1111AA")));
    }

    @Test
    void deletePerson() throws Exception {

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        mockMvc.perform(delete("/persons/{id}", person.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePersonUnknownId() throws Exception {
        mockMvc.perform(delete("/persons/{id}", "111").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No person with given ID found."));
    }

    @Test
    void deletePersonWithDog() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);
        dogs.add(domesticatedDog);

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        person.setDogs(dogs);
        personRepository.save(person);

        mockMvc.perform(delete("/persons/{id}", person.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletePersonWithWaitingListItem() throws Exception {

        waitingListItem.setNumberOnList(1);
        waitingListItem.setSex(Sex.female);
        waitingListItem.setBreed(Breed.Dachschund);
        waitingListItem.setKindOfHair("Long Haired");
        waitingListItemList.add(waitingListItem);

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        person.setDogs(dogs);
        person.setWaitingListItems(waitingListItemList);
        personRepository.save(person);

        mockMvc.perform(delete("/persons/{id}", person.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }

    //TEST HELPER CLASSES
    public static String asJsonString(final PersonInputDto obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String arrayAsJsonString(final List<PersonInputDto> obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
