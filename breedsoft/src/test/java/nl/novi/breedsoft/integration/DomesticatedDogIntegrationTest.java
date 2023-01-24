package nl.novi.breedsoft.integration;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.Person;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.BreedGroup;
import nl.novi.breedsoft.model.management.enumerations.Status;
import nl.novi.breedsoft.repository.DomesticatedDogRepository;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.service.DomesticatedDogService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DomesticatedDogIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DomesticatedDogService domesticatedDogService;

    @Autowired
    DomesticatedDogRepository domesticatedDogRepository;

    @Autowired
    PersonRepository personRepository;

    DomesticatedDog domesticatedDog1 = new DomesticatedDog();
    DomesticatedDog domesticatedDog2 = new DomesticatedDog();
    DomesticatedDog domesticatedDog3 = new DomesticatedDog();
    DomesticatedDog domesticatedDog4 = new DomesticatedDog();

    DomesticatedDogOutputDto dogOutputDto1 = new DomesticatedDogOutputDto();


    Person person = new Person();

    @BeforeEach
    void setup() {
        person.setId(1L);
        person.setSex(Sex.female);
        person.setFirstName("Eva");
        person.setLastName("Hauber");
        person.setStreet("Maas");
        person.setHouseNumber(31);
        person.setZipCode("5172CN");
        person.setCity("Kaatsheuvel");
        personRepository.save(person);

        domesticatedDog1.setId(1L);
        domesticatedDog1.setName("Saar");
        domesticatedDog1.setBreed(Breed.Dachschund);
        domesticatedDog1.setFood("dog chow");
        domesticatedDog1.setDogStatus(Status.breedDog);
        domesticatedDog1.setPerson(person);
        domesticatedDog1.setSex(Sex.female);
        domesticatedDog1.setBreedGroup(BreedGroup.Hound);
        domesticatedDog1.setKindOfHair("Long haired");

        domesticatedDog2.setId(2L);
        domesticatedDog2.setName("Pip");
        domesticatedDog2.setBreed(Breed.Dachschund);
        domesticatedDog2.setFood("dog chow");
        domesticatedDog2.setDogStatus(Status.ownedDog);
        domesticatedDog2.setPerson(person);
        domesticatedDog2.setSex(Sex.female);
        domesticatedDog2.setBreedGroup(BreedGroup.Hound);
        domesticatedDog2.setKindOfHair("Long haired");


        domesticatedDog3.setId(3L);
        domesticatedDog3.setName("Lotje");
        domesticatedDog3.setBreed(Breed.Affenpinscher);
        domesticatedDog3.setFood("dog chow");
        domesticatedDog3.setDogStatus(Status.soldPup);
        domesticatedDog3.setPerson(person);
        domesticatedDog3.setSex(Sex.female);
        domesticatedDog3.setBreedGroup(BreedGroup.Hound);
        domesticatedDog3.setKindOfHair("Short haired");
        domesticatedDog3.setParentId(1L);

        domesticatedDog4.setId(4L);
        domesticatedDog4.setName("Pupje");
        domesticatedDog4.setBreed(Breed.Affenpinscher);
        domesticatedDog4.setFood("milk");
        domesticatedDog4.setDogStatus(Status.soldPup);
        domesticatedDog4.setSex(Sex.female);
        domesticatedDog4.setBreedGroup(BreedGroup.Hound);
        domesticatedDog4.setKindOfHair("Short haired");


        domesticatedDogRepository.save(domesticatedDog1);
        domesticatedDogRepository.save(domesticatedDog2);
        domesticatedDogRepository.save(domesticatedDog3);
        domesticatedDogRepository.save(domesticatedDog4);

        dogOutputDto1.setId(domesticatedDog1.getId());
        dogOutputDto1.setName("Saar");
        dogOutputDto1.setBreed(Breed.Dachschund);
        dogOutputDto1.setFood("dog chow");
        dogOutputDto1.setDogStatus(Status.breedDog);
        dogOutputDto1.setPerson(person);
        dogOutputDto1.setSex(Sex.female);
        dogOutputDto1.setBreedGroup(BreedGroup.Hound);
        dogOutputDto1.setKindOfHair("Long haired");
    }

    @AfterEach
    void tear(){
        domesticatedDogService = null;
        domesticatedDogRepository = null;
        domesticatedDog1 = null;
        domesticatedDog2 = null;
        domesticatedDog3 = null;
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
    void getDomesticatedDogByName() throws Exception {
        mockMvc.perform(get("/dogs/name/{name}", "Lotje").with(jwt()))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Lotje"))
                .andExpect(jsonPath("$[0].sex").value("female"));
    }

    @Test
    void getChildrenById() throws Exception {
        mockMvc.perform(get("/dogs/{id}/children", "1").with(jwt()))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].name").value("Lotje"))
                .andExpect(jsonPath("$[0].sex").value("female"));
    }

    @Test
    void deleteDog() throws Exception {
        mockMvc.perform(delete("/dogs/{id}", "4").with(jwt()))
                .andExpect(status().isNoContent());
    }

}
