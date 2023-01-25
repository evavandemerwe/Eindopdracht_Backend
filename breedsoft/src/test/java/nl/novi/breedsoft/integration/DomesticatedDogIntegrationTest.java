package nl.novi.breedsoft.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    DomesticatedDog domesticatedDog1;
    DomesticatedDog domesticatedDog2;
    DomesticatedDog domesticatedDog3;
    DomesticatedDog domesticatedDog4;

    List<DomesticatedDogInputDto> domesticatedDogInputDtoList;

    DomesticatedDogOutputDto dogOutputDto1;

    DomesticatedDogInputDto dogInputDto;
    DomesticatedDogInputDto dogInputDtoUnknownPerson;
    DomesticatedDogInputDto dogInputDtoUnknownSex;
    DomesticatedDogInputDto dogInputDtoMissingValues;
    DomesticatedDogInputDto dogInputDtoLostWeight;

    Person person;
    Person person2;
    Person personNotInRepo;

    VeterinarianAppointment veterinarianAppointment;
    List<VeterinarianAppointment> veterinarianAppointmentList;
    MedicalData medicalData;
    List<MedicalData> medicalDataList;

    @BeforeEach
    void setup() {


        personNotInRepo = new Person();
        domesticatedDog1 = new DomesticatedDog();
        domesticatedDog2 = new DomesticatedDog();
        domesticatedDog3 = new DomesticatedDog();
        domesticatedDog4 = new DomesticatedDog();
        person = new Person();
        person2 = new Person();
        dogOutputDto1 = new DomesticatedDogOutputDto();
        dogInputDto = new DomesticatedDogInputDto();
        domesticatedDogInputDtoList = new ArrayList<>();
        dogInputDtoLostWeight = new DomesticatedDogInputDto();
        dogInputDtoUnknownPerson = new DomesticatedDogInputDto();
        dogInputDtoUnknownSex = new DomesticatedDogInputDto();
        dogInputDtoMissingValues = new DomesticatedDogInputDto();
        medicalData = new MedicalData();
        medicalDataList = new ArrayList<>();
        veterinarianAppointment = new VeterinarianAppointment();
        veterinarianAppointmentList = new ArrayList<>();

        person = new Person();
        person.setId(1L);
        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        person.setDogs(new ArrayList<>(List.of(domesticatedDog1, domesticatedDog2, domesticatedDog3)));
        personRepository.save(person);

        domesticatedDog1.setId(1L);
        domesticatedDog1.setName("Saar");
        domesticatedDog1.setBreed(Breed.Dachschund);
        domesticatedDog1.setFood("dog chow");
        domesticatedDog1.setDogStatus(Status.breedDog);
        domesticatedDog1.setSex(Sex.female);
        domesticatedDog1.setBreedGroup(BreedGroup.Hound);
        domesticatedDog1.setKindOfHair("Long haired");
        domesticatedDog1.setWeightInGrams(1000.00);
        domesticatedDogRepository.save(domesticatedDog1);

        domesticatedDog2.setId(2L);
        domesticatedDog2.setName("Pip");
        domesticatedDog2.setBreed(Breed.Dachschund);
        domesticatedDog2.setFood("dog chow");
        domesticatedDog2.setDogStatus(Status.ownedDog);
        domesticatedDog2.setSex(Sex.female);
        domesticatedDog2.setBreedGroup(BreedGroup.Hound);
        domesticatedDog2.setKindOfHair("Long haired");
        domesticatedDogRepository.save(domesticatedDog2);

        domesticatedDog3.setId(3L);
        domesticatedDog3.setName("Lotje");
        domesticatedDog3.setBreed(Breed.Affenpinscher);
        domesticatedDog3.setFood("dog chow");
        domesticatedDog3.setDogStatus(Status.availablePreOwned);
        domesticatedDog3.setSex(Sex.female);
        domesticatedDog3.setBreedGroup(BreedGroup.Hound);
        domesticatedDog3.setKindOfHair("Short haired");
        domesticatedDog3.setParentId(1L);
        domesticatedDogRepository.save(domesticatedDog3);


    }

    @AfterEach
    void tear(){

        if(domesticatedDogRepository.count() > 0){
            domesticatedDogRepository = null;
        }

        if(personRepository.count() > 0){
            personRepository = null;
        }

    }

    @Test
    void getAllDomesticatedDogs() throws Exception {

        mockMvc.perform(get("/dogs").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Saar"))
                .andExpect(jsonPath("$[0].sex").value("female"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].breedGroup").value("Hound"))
                .andExpect(jsonPath("$[1].kindOfHair").value("Long haired"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("Lotje"));
    }

    @Test
    void getDomesticatedDogById() throws Exception {
        mockMvc.perform(get("/dogs/id/{id}", "1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
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
        mockMvc.perform(get("/dogs/name/{name}", "Lotje").with(jwt()))
                .andExpect(jsonPath("$[0].id").value(3))
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
        mockMvc.perform(get("/dogs/{id}/children", "1").with(jwt()))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Lotje"))
                .andExpect(jsonPath("$[0].sex").value("female"));
    }

    @Test
    void getChildrenByIdWithoutChildren() throws Exception {
        mockMvc.perform(get("/dogs/{id}/children", "2").with(jwt()))
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
        mockMvc.perform(get("/dogs/{id}/parent", "3").with(jwt()))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Saar"))
                .andExpect(jsonPath("$.sex").value("female"));
    }

    @Test
    void getParentByIdWithoutParent() throws Exception {
        mockMvc.perform(get("/dogs/{id}/parent", "1").with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No information about parent found."));
    }

    @Test
    void getAvailableDogs() throws Exception {
        mockMvc.perform(get("/dogs/available").with(jwt()))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Lotje"))
                .andExpect(jsonPath("$[0].sex").value("female"));
    }

    @Test
    void getBreedDogs() throws Exception {
        mockMvc.perform(get("/dogs/breeddog").with(jwt()))
                .andExpect(jsonPath("$[0].id").value(1))
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
        dogInputDto.setPerson(person);
        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));
        dogInputDto.setParentId(2L);

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
        dogInputDto.setName("Lotje");
        dogInputDto.setBreed("Dachschund");
        dogInputDto.setFood("dog chow");
        dogInputDto.setDogStatus("breedDog");
        dogInputDto.setPerson(person);
        dogInputDto.setSex("female");
        dogInputDto.setBreedGroup("Hound");
        dogInputDto.setKindOfHair("Long haired");
        dogInputDto.setChipNumber("333333333333333");
        dogInputDto.setHairColor("Brown");
        dogInputDto.setDateOfBirth(LocalDate.of(2022, Month.JANUARY, 22));

        domesticatedDogInputDtoList = new ArrayList<>();
        domesticatedDogInputDtoList.add(dogInputDto);

        mockMvc.perform(post("/dogs/{id}/children", 1).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(arrayAsJsonString(domesticatedDogInputDtoList)))
                .andExpect(status().isOk());
    }

    @Test
    void createLitterWithNonListInput() throws Exception {
        mockMvc.perform(post("/dogs/{id}/children", 1).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createLitterWithEmptyListInput() throws Exception {
        List<DomesticatedDogInputDto> dogs = new ArrayList<>();

        mockMvc.perform(post("/dogs/{id}/children", 1).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(arrayAsJsonString(dogs)))
                .andExpect(content().string("Empty array. No dogs are created"));
    }


    @Test
    void uploadImageWithoutImage() throws Exception {
        byte[] imageBytes = {1, 2, 3};
        MockMultipartFile image = new MockMultipartFile("file", "filename.txt", "text/plain", imageBytes);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/dogs/{id}/image", 1)
                .file(image).with(jwt()))
                .andExpect(status().isBadRequest());
   }

    @Test
    void updateDog() throws Exception {
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

        mockMvc.perform(put("/dogs/{id}", "3").with(jwt())
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

        mockMvc.perform(put("/dogs/{id}", "3").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoUnknownPerson)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Provided dog owner does not exist"));
    }

    @Test
    void patchDog() throws Exception {
        mockMvc.perform(patch("/dogs/{id}", "3").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void patchDogWithUnknownId() throws Exception {
        mockMvc.perform(patch("/dogs/{id}", "666").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDto)))
                .andExpect(content().string("Dog is not found."));
    }

    @Test
    void patchDogWithLostWeight() throws Exception {

        dogInputDtoLostWeight.setWeightInGrams(10.00);

        mockMvc.perform(patch("/dogs/{id}", "1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoLostWeight)))
                .andExpect(status().isOk())
                .andExpect(content().string("Your dog has been updated. WARNING: Your dog has lost weight!"));
    }

    @Test
    void patchDogWithUnknownPerson() throws Exception {

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

        mockMvc.perform(patch("/dogs/{id}", "1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(dogInputDtoUnknownPerson)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Provided dog owner does not exist"));
    }


    @Test
    void deleteDogImageWithoutImage() throws Exception {
        mockMvc.perform(delete("/dogs/{id}/image", "1").with(jwt()))
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
        DomesticatedDog domesticatedDogToDelete = new DomesticatedDog();
        domesticatedDogToDelete.setId(4L);
        domesticatedDogToDelete.setName("Pupje");
        domesticatedDogToDelete.setBreed(Breed.Affenpinscher);
        domesticatedDogToDelete.setFood("milk");
        domesticatedDogToDelete.setDogStatus(Status.soldPup);
        domesticatedDogToDelete.setSex(Sex.female);
        domesticatedDogToDelete.setBreedGroup(BreedGroup.Hound);
        domesticatedDogToDelete.setKindOfHair("Short haired");
        domesticatedDogRepository.save(domesticatedDogToDelete);

        medicalData = new MedicalData();
        medicalData.setId(1L);
        medicalData.setDiagnose("Sick");
        medicalData.setDateOfMedicalTreatment(LocalDate.of(2023, Month.JANUARY, 22));
        medicalData.setDomesticatedDog(domesticatedDogToDelete);
        medicalDataRepository.save(medicalData);

        veterinarianAppointment = new VeterinarianAppointment();
        veterinarianAppointment.setId(1L);
        veterinarianAppointment.setSubject("Check up");
        veterinarianAppointment.setAppointmentDate(LocalDate.of(2023, Month.JANUARY, 22));
        veterinarianAppointment.setDomesticatedDog(domesticatedDogToDelete);
        veterinarianAppointmentRepository.save(veterinarianAppointment);

        mockMvc.perform(delete("/dogs/{id}", domesticatedDogToDelete.getId()).with(jwt()))
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
