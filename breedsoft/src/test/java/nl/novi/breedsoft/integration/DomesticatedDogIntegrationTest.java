package nl.novi.breedsoft.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.breedsoft.cleanupH2DatabaseTestListener.CleanupH2DatabaseTestListener;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.MedicalData;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.VeterinarianAppointment;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.BreedGroup;
import nl.novi.breedsoft.model.management.enumerations.Status;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.repository.MedicalDataRepository;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.repository.VeterinarianAppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
public class DomesticatedDogIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DomesticatedDogRepository domesticatedDogRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    MedicalDataRepository medicalDataRepository;

    @Autowired
    VeterinarianAppointmentRepository veterinarianAppointmentRepository;

    DomesticatedDog domesticatedDog = new DomesticatedDog();
    DomesticatedDog domesticatedDogPup = new DomesticatedDog();
    DomesticatedDog domesticatedDogPreOwned = new DomesticatedDog();

    List<DomesticatedDogInputDto> domesticatedDogInputDtoList = new ArrayList<>();

    DomesticatedDogInputDto dogInputDto = new DomesticatedDogInputDto();
    DomesticatedDogInputDto dogInputDtoUnknownPerson = new DomesticatedDogInputDto();
    DomesticatedDogInputDto dogInputDtoUnknownSex = new DomesticatedDogInputDto();
    DomesticatedDogInputDto dogInputDtoMissingValues = new DomesticatedDogInputDto();
    DomesticatedDogInputDto dogInputDtoLostWeight = new DomesticatedDogInputDto();

    Person person = new Person();
    Person personNotInRepo = new Person();
    VeterinarianAppointment veterinarianAppointment = new VeterinarianAppointment();
    MedicalData medicalData = new MedicalData();

    @BeforeEach
    void setup(){

        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);
    }

    @Test
    void getAllDomesticatedDogs() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/dogs").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(domesticatedDog.getId()))
                .andExpect(jsonPath("$[0].name").value("Saar"))
                .andExpect(jsonPath("$[0].sex").value("female"));
    }

    @Test
    void getDomesticatedDogById() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/dogs/id/{id}", domesticatedDog.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(domesticatedDog.getId()))
                .andExpect(jsonPath("$.name").value("Saar"))
                .andExpect(jsonPath("$.sex").value("female"));
    }

    @Test
    void getDomesticatedDogByIdWithUnknownId() throws Exception {
        mockMvc.perform(get("/dogs/id/{id}", "1000").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dog not found in database."));
    }

    @Test
    void getDomesticatedDogByName() throws Exception {

        domesticatedDog.setName("Lotje");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/dogs/name/{name}", "Lotje").with(jwt()))
                .andExpect(jsonPath("$[0].id").value(domesticatedDog.getId()))
                .andExpect(jsonPath("$[0].name").value("Lotje"))
                .andExpect(jsonPath("$[0].sex").value("female"));
    }

    @Test
    void getDomesticatedDogByNameWithUnknownName() throws Exception {
        mockMvc.perform(get("/dogs/name/{name}", "xxx").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No dog with this name found in database."));
    }


    @Test
    void getChildrenById() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        domesticatedDogPup.setName("Lotje");
        domesticatedDogPup.setBreed(Breed.Dachschund);
        domesticatedDogPup.setFood("dog chow");
        domesticatedDogPup.setDogStatus(Status.breedDog);
        domesticatedDogPup.setSex(Sex.female);
        domesticatedDogPup.setBreedGroup(BreedGroup.Hound);
        domesticatedDogPup.setKindOfHair("Long haired");
        domesticatedDogPup.setParentId(domesticatedDog.getId());
        domesticatedDogRepository.save(domesticatedDogPup);

        mockMvc.perform(get("/dogs/{id}/children", domesticatedDog.getId()).with(jwt()))
                .andExpect(jsonPath("$[0].id").value(domesticatedDogPup.getId()))
                .andExpect(jsonPath("$[0].name").value("Lotje"))
                .andExpect(jsonPath("$[0].sex").value("female"));
    }

    @Test
    void getChildrenByIdWithoutChildren() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/dogs/{id}/children", domesticatedDog.getId()).with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("This dog doesn't have children."));
    }

    @Test
    void getChildrenByIdWithUnknownParent() throws Exception {
        mockMvc.perform(get("/dogs/{id}/children", "1000").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No dog with this id found in database."));
    }

    @Test
    void getParentById() throws Exception {
        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        domesticatedDogPup.setName("Lotje");
        domesticatedDogPup.setBreed(Breed.Dachschund);
        domesticatedDogPup.setFood("dog chow");
        domesticatedDogPup.setDogStatus(Status.availablePup);
        domesticatedDogPup.setSex(Sex.female);
        domesticatedDogPup.setBreedGroup(BreedGroup.Hound);
        domesticatedDogPup.setKindOfHair("Long haired");
        domesticatedDogPup.setParentId(domesticatedDog.getId());
        domesticatedDogRepository.save(domesticatedDogPup);

        mockMvc.perform(get("/dogs/{id}/parent", domesticatedDogPup.getId()).with(jwt()))
                .andExpect(jsonPath("$.id").value(domesticatedDog.getId()))
                .andExpect(jsonPath("$.name").value("Saar"))
                .andExpect(jsonPath("$.sex").value("female"));
    }

    @Test
    void getParentByIdWithoutParent() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/dogs/{id}/parent", domesticatedDog.getId()).with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No information about parent found."));
    }

    @Test
    void getAvailableDogs() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        domesticatedDogPup.setName("Lotje");
        domesticatedDogPup.setBreed(Breed.Dachschund);
        domesticatedDogPup.setFood("dog chow");
        domesticatedDogPup.setDogStatus(Status.availablePup);
        domesticatedDogPup.setSex(Sex.female);
        domesticatedDogPup.setBreedGroup(BreedGroup.Hound);
        domesticatedDogPup.setKindOfHair("Long haired");
        domesticatedDogPup.setParentId(domesticatedDog.getId());
        domesticatedDogRepository.save(domesticatedDogPup);

        domesticatedDogPreOwned.setName("Pip");
        domesticatedDogPreOwned.setBreed(Breed.Dachschund);
        domesticatedDogPreOwned.setFood("dog chow");
        domesticatedDogPreOwned.setDogStatus(Status.availablePreOwned);
        domesticatedDogPreOwned.setSex(Sex.male);
        domesticatedDogPreOwned.setBreedGroup(BreedGroup.Hound);
        domesticatedDogPreOwned.setKindOfHair("Long haired");

        domesticatedDogRepository.save(domesticatedDogPreOwned);
        mockMvc.perform(get("/dogs/available").with(jwt()))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Pip")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Lotje")));

    }

    @Test
    void getBreedDogs() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setPerson(person);
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(get("/dogs/breeddog").with(jwt()))
                .andExpect(jsonPath("$[0].id").value(domesticatedDog.getId()))
                .andExpect(jsonPath("$[0].name").value("Saar"))
                .andExpect(jsonPath("$[0].sex").value("female"));
    }

    @Test
    void createDog() throws Exception {

        dogInputDto.setName("Saar");
        dogInputDto.setBreed("Dachschund");
        dogInputDto.setFood("dog chow");
        dogInputDto.setDogStatus("breedDog");
        dogInputDto.setSex("female");
        dogInputDto.setBreedGroup("Hound");
        dogInputDto.setKindOfHair("Long haired");
        dogInputDto.setChipNumber("111111111111111");
        dogInputDto.setHairColor("Brown");
        domesticatedDog.setPerson(person);
        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));

        mockMvc.perform(post("/dogs").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dogInputDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void createDogWithoutDogObject() throws Exception {

        mockMvc.perform(post("/dogs").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void createDogMissingHairColor() throws Exception {

        dogInputDtoMissingValues.setName("Saar");
        dogInputDtoMissingValues.setBreed("Dachschund");
        dogInputDtoMissingValues.setFood("dog chow");
        dogInputDtoMissingValues.setDogStatus("breedDog");
        dogInputDtoMissingValues.setSex("female");
        dogInputDtoMissingValues.setBreedGroup("Hound");
        dogInputDtoMissingValues.setPerson(person);

        mockMvc.perform(post("/dogs").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dogInputDtoMissingValues)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("hairColor: Please enter the hair color of the dog.")));
    }

    @Test
    void createDogUnknownParent() throws Exception {

        dogInputDto.setName("Saar");
        dogInputDto.setBreed("Dachschund");
        dogInputDto.setFood("dog chow");
        dogInputDto.setDogStatus("breedDog");
        dogInputDto.setSex("female");
        dogInputDto.setBreedGroup("Hound");
        dogInputDto.setKindOfHair("Long haired");
        dogInputDto.setChipNumber("111111111111111");
        dogInputDto.setHairColor("Brown");
        domesticatedDog.setPerson(person);

        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));
        dogInputDto.setParentId(222L);

        mockMvc.perform(post("/dogs").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Parent ID not found.")));
    }

    @Test
    void createDogMissingDOB() throws Exception {

        dogInputDtoMissingValues.setName("Saar");
        dogInputDtoMissingValues.setBreed("Dachschund");
        dogInputDtoMissingValues.setFood("dog chow");
        dogInputDtoMissingValues.setDogStatus("breedDog");
        dogInputDtoMissingValues.setSex("female");
        dogInputDtoMissingValues.setBreedGroup("Hound");

        mockMvc.perform(post("/dogs").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoMissingValues)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("dateOfBirth: must not be null")));
    }

    @Test
    void createDogMissingHairKind() throws Exception {

        dogInputDtoMissingValues.setName("Saar");
        dogInputDtoMissingValues.setBreed("Dachschund");
        dogInputDtoMissingValues.setFood("dog chow");
        dogInputDtoMissingValues.setDogStatus("breedDog");
        dogInputDtoMissingValues.setSex("female");
        dogInputDtoMissingValues.setBreedGroup("Hound");

        mockMvc.perform(post("/dogs").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoMissingValues)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("kindOfHair: Please enter kind of hair.")));
    }

    @Test
    void createDogMissingChipNumber() throws Exception {

        dogInputDtoMissingValues.setName("Saar");
        dogInputDtoMissingValues.setBreed("Dachschund");
        dogInputDtoMissingValues.setFood("dog chow");
        dogInputDtoMissingValues.setDogStatus("breedDog");
        dogInputDtoMissingValues.setSex("female");
        dogInputDtoMissingValues.setBreedGroup("Hound");

        mockMvc.perform(post("/dogs").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoMissingValues)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("chipNumber: Please enter a chip number.")));
    }

    @Test
    void createDogInvalidPersonId() throws Exception {

        personNotInRepo.setId(5L);
        dogInputDtoUnknownPerson.setName("Saar");
        dogInputDtoUnknownPerson.setBreed("Dachschund");
        dogInputDtoUnknownPerson.setFood("dog chow");
        dogInputDtoUnknownPerson.setDogStatus("breedDog");
        dogInputDtoUnknownPerson.setSex("female");
        dogInputDtoUnknownPerson.setBreedGroup("Hound");
        dogInputDtoUnknownPerson.setKindOfHair("Long haired");
        dogInputDtoUnknownPerson.setChipNumber("111111111111111");
        dogInputDtoUnknownPerson.setHairColor("Brown");
        dogInputDtoUnknownPerson.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));
        dogInputDtoUnknownPerson.setPerson(personNotInRepo);

        mockMvc.perform(post("/dogs").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoUnknownPerson)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Provided dog owner does not exist."));
    }


    @Test
    void createDogMissingPersonId() throws Exception {

        dogInputDtoUnknownPerson.setName("Saar");
        dogInputDtoUnknownPerson.setBreed("Dachschund");
        dogInputDtoUnknownPerson.setFood("dog chow");
        dogInputDtoUnknownPerson.setDogStatus("breedDog");
        dogInputDtoUnknownPerson.setSex("female");
        dogInputDtoUnknownPerson.setBreedGroup("Hound");
        dogInputDtoUnknownPerson.setKindOfHair("Long haired");
        dogInputDtoUnknownPerson.setChipNumber("111111111111111");
        dogInputDtoUnknownPerson.setHairColor("Brown");
        dogInputDtoUnknownPerson.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));
        dogInputDtoUnknownPerson.setPerson(personNotInRepo);

        mockMvc.perform(post("/dogs").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoUnknownPerson)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Missing Person ID"));
    }


    @Test
    void createDogInvalidSex() throws Exception {

        dogInputDtoUnknownSex.setName("Saar");
        dogInputDtoUnknownSex.setBreed("Dachschund");
        dogInputDtoUnknownSex.setFood("dog chow");
        dogInputDtoUnknownSex.setDogStatus("breedDog");
        dogInputDtoUnknownSex.setSex("xxx");
        dogInputDtoUnknownSex.setBreedGroup("Hound");
        dogInputDtoUnknownSex.setKindOfHair("Long haired");
        dogInputDtoUnknownSex.setChipNumber("111111111111111");
        dogInputDtoUnknownSex.setHairColor("Brown");
        dogInputDtoUnknownSex.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));

        mockMvc.perform(post("/dogs").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoUnknownSex)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("sex: Invalid sex.")));
    }

    @Test
    void createLitter() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        dogInputDto.setName("Lotje");
        dogInputDto.setBreed("Dachschund");
        dogInputDto.setFood("dog chow");
        dogInputDto.setDogStatus("breedDog");
        dogInputDto.setSex("female");
        dogInputDto.setBreedGroup("Hound");
        dogInputDto.setKindOfHair("Long haired");
        dogInputDto.setChipNumber("333333333333333");
        dogInputDto.setHairColor("Brown");
        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));

        domesticatedDogInputDtoList = new ArrayList<>();
        domesticatedDogInputDtoList.add(dogInputDto);

        mockMvc.perform(post("/dogs/{id}/children", domesticatedDog.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(arrayAsJsonString(domesticatedDogInputDtoList)))
                .andExpect(status().isOk());
    }

    @Test
    void createLitterWithNonListInput() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(post("/dogs/{id}/children", domesticatedDog.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLitterWithEmptyListInput() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        List<DomesticatedDogInputDto> dogs = new ArrayList<>();

        mockMvc.perform(post("/dogs/{id}/children", domesticatedDog.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(arrayAsJsonString(dogs)))
                .andExpect(content().string("Empty array. No dogs are created"));
    }


    @Test
    void uploadImageWithoutImage() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        byte[] imageBytes = {1, 2, 3};
        MockMultipartFile image = new MockMultipartFile(
                "file",
                "filename.txt",
                "text/plain",
                imageBytes);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/dogs/{id}/image", domesticatedDog.getId())
                .file(image).with(jwt()))
                .andExpect(status().isBadRequest());
   }

    @Test
    void updateDog() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        dogInputDto.setName("Saar");
        dogInputDto.setBreed("Dachschund");
        dogInputDto.setFood("dog chow");
        dogInputDto.setDogStatus("breedDog");
        dogInputDto.setPerson(person);
        dogInputDto.setSex("female");
        dogInputDto.setBreedGroup("Hound");
        dogInputDto.setKindOfHair("Long haired");
        dogInputDto.setChipNumber("111111111111111");
        dogInputDto.setHairColor("Brown");
        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));

        mockMvc.perform(put("/dogs/{id}", domesticatedDog.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateDogWithUnknownId() throws Exception {

        dogInputDto.setName("Saar");
        dogInputDto.setBreed("Dachschund");
        dogInputDto.setFood("dog chow");
        dogInputDto.setDogStatus("breedDog");
        dogInputDto.setPerson(person);
        dogInputDto.setSex("female");
        dogInputDto.setBreedGroup("Hound");
        dogInputDto.setKindOfHair("Long haired");
        dogInputDto.setChipNumber("111111111111111");
        dogInputDto.setHairColor("Brown");
        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));

        mockMvc.perform(put("/dogs/{id}", "555").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateDogWithUnknownPerson() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);
        personNotInRepo.setId(5L);

        dogInputDtoUnknownPerson.setName("Saar");
        dogInputDtoUnknownPerson.setBreed("Dachschund");
        dogInputDtoUnknownPerson.setFood("dog chow");
        dogInputDtoUnknownPerson.setDogStatus("breedDog");
        dogInputDtoUnknownPerson.setSex("female");
        dogInputDtoUnknownPerson.setBreedGroup("Hound");
        dogInputDtoUnknownPerson.setKindOfHair("Long haired");
        dogInputDtoUnknownPerson.setChipNumber("111111111111111");
        dogInputDtoUnknownPerson.setHairColor("Brown");
        dogInputDtoUnknownPerson.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));
        dogInputDtoUnknownPerson.setPerson(personNotInRepo);

        mockMvc.perform(put("/dogs/{id}", domesticatedDog.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoUnknownPerson)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Provided dog owner does not exist"));
    }

    @Test
    void patchDog() throws Exception {
        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        dogInputDto.setName("Saar");
        dogInputDto.setBreed("Dachschund");
        dogInputDto.setFood("dog chow");
        dogInputDto.setDogStatus("breedDog");
        dogInputDto.setPerson(person);
        dogInputDto.setSex("female");
        dogInputDto.setBreedGroup("Hound");
        dogInputDto.setKindOfHair("Long haired");
        dogInputDto.setChipNumber("111111111111111");
        dogInputDto.setHairColor("Brown");
        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));

        mockMvc.perform(patch("/dogs/{id}", domesticatedDog.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void patchDogWithUnknownId() throws Exception {

        dogInputDto.setName("Saar");
        dogInputDto.setBreed("Dachschund");
        dogInputDto.setFood("dog chow");
        dogInputDto.setDogStatus("breedDog");
        dogInputDto.setPerson(person);
        dogInputDto.setSex("female");
        dogInputDto.setBreedGroup("Hound");
        dogInputDto.setKindOfHair("Long haired");
        dogInputDto.setChipNumber("111111111111111");
        dogInputDto.setHairColor("Brown");
        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));

        mockMvc.perform(patch("/dogs/{id}", "666").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(content().string("Dog is not found."));
    }

    @Test
    void patchDogWithLostWeight() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDog.setWeightInGrams(1000.99);
        domesticatedDogRepository.save(domesticatedDog);

        dogInputDtoLostWeight.setWeightInGrams(10.00);

        mockMvc.perform(patch("/dogs/{id}", domesticatedDog.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoLostWeight)))
                .andExpect(status().isOk())
                .andExpect(content().string("Your dog has been updated. WARNING: Your dog has lost weight!"));
    }

    @Test
    void patchDogWithUnknownPerson() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        personNotInRepo.setId(5L);

        dogInputDtoUnknownPerson.setName("Saar");
        dogInputDtoUnknownPerson.setBreed("Dachschund");
        dogInputDtoUnknownPerson.setFood("dog chow");
        dogInputDtoUnknownPerson.setDogStatus("breedDog");
        dogInputDtoUnknownPerson.setSex("female");
        dogInputDtoUnknownPerson.setBreedGroup("Hound");
        dogInputDtoUnknownPerson.setKindOfHair("Long haired");
        dogInputDtoUnknownPerson.setChipNumber("111111111111111");
        dogInputDtoUnknownPerson.setHairColor("Brown");
        dogInputDtoUnknownPerson.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));
        dogInputDtoUnknownPerson.setPerson(personNotInRepo);

        mockMvc.perform(patch("/dogs/{id}", domesticatedDog.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoUnknownPerson)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Provided dog owner does not exist"));
    }


    @Test
    void deleteDogImageWithoutImage() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        mockMvc.perform(delete("/dogs/{id}/image", domesticatedDog.getId()).with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("This dog does not have a picture."));
    }

    @Test
    void deleteDogImageUnknownId() throws Exception {
        mockMvc.perform(delete("/dogs/{id}/image", "111").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No dog with given ID found."));
    }

    @Test
    void deleteDogUnknownId() throws Exception {
        mockMvc.perform(delete("/dogs/{id}", "111").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No dog with given ID found."));
    }

    @Test
    void deleteDog() throws Exception {

        domesticatedDog.setName("Saar");
        domesticatedDog.setBreed(Breed.Dachschund);
        domesticatedDog.setFood("dog chow");
        domesticatedDog.setDogStatus(Status.breedDog);
        domesticatedDog.setSex(Sex.female);
        domesticatedDog.setBreedGroup(BreedGroup.Hound);
        domesticatedDog.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog);

        medicalData = new MedicalData();
        medicalData.setId(1L);
        medicalData.setDiagnose("Sick");
        medicalData.setDateOfMedicalTreatment(LocalDate.of(2023, Month.JANUARY, 22));
        medicalData.setDomesticatedDog(domesticatedDog);
        medicalDataRepository.save(medicalData);

        veterinarianAppointment = new VeterinarianAppointment();
        veterinarianAppointment.setId(1L);
        veterinarianAppointment.setSubject("Check up");
        veterinarianAppointment.setAppointmentDate(LocalDate.of(2023, Month.JANUARY, 22));
        veterinarianAppointment.setDomesticatedDog(domesticatedDog);
        veterinarianAppointmentRepository.save(veterinarianAppointment);

        mockMvc.perform(delete("/dogs/{id}", domesticatedDog.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }

    //TEST HELPER CLASSES
    public static String asJsonString(final DomesticatedDogInputDto obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String arrayAsJsonString(final List<DomesticatedDogInputDto> obj) {
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
