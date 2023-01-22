package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.personDtos.PersonInputDto;
import nl.novi.breedsoft.dto.personDtos.PersonOutputDto;
import nl.novi.breedsoft.dto.personDtos.PersonPatchDto;
import nl.novi.breedsoft.model.animal.enumerations.Sex;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.model.management.enumerations.Status;
import nl.novi.breedsoft.service.PersonService;
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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PersonService personService;

    PersonOutputDto personOutputDto = new PersonOutputDto();
    PersonInputDto personInputDto = new PersonInputDto();
    PersonPatchDto personPatchDto = new PersonPatchDto();
    List<PersonOutputDto> personOutputDtoList = new ArrayList<>();

    DomesticatedDog dog = new DomesticatedDog();
    List<DomesticatedDog> dogs = new ArrayList<>();
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

        personOutputDto.setSex(Sex.female);
        personOutputDto.setFirstName("Eva");
        personOutputDto.setLastName("Hauber");
        personOutputDto.setStreet("Maas");
        personOutputDto.setHouseNumber(31);
        personOutputDto.setZipCode("5172CN");
        personOutputDto.setCity("Kaatsheuvel");
        personOutputDto.setDogs(dogs);
        personOutputDtoList.add(personOutputDto);

        personInputDto.setSex("female");
        personInputDto.setFirstName("Eva");
        personInputDto.setLastName("Hauber");
        personInputDto.setStreet("Maas");
        personInputDto.setHouseNumber(31);
        personInputDto.setZipCode("5172CN");
        personInputDto.setCity("Kaatsheuvel");
        personInputDto.setDogs(dogs);

        personPatchDto.setSex("female");
        personPatchDto.setFirstName("Eva");
        personPatchDto.setLastName("Hauber");
        personPatchDto.setStreet("Maas");
        personPatchDto.setHouseNumber(31);
        personPatchDto.setZipCode("5172CN");
        personPatchDto.setCity("Kaatsheuvel");

    }

    @AfterEach
    void tear(){
        context = null;
        mockMvc = null;
        personService = null;
        personPatchDto = null;
        personInputDto = null;
        personOutputDto= null;
        personOutputDtoList = null;
    }

    @Test
    void getAllPersons() throws Exception {
        when(personService.getAllPersons()).
                thenReturn(personOutputDtoList);
            this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/persons")
                            .with(jwt()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.
                            jsonPath("$.[0].firstName", is("Eva")));
    }

    @Test
    void getPersonById() throws Exception {
        when(personService.getPersonById(1L)).
                thenReturn(personOutputDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/id/{id}", "1")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.firstName", is("Eva")));
    }

    @Test
    void getPersonByName() throws Exception {
        when(personService.getPersonByName("Hauber")).
                thenReturn(personOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/name/{name}", "Hauber")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].firstName", is("Eva")));
    }

    @Test
    void getDogBreeders() throws Exception {
        when(personService.getDogBreeders()).
                thenReturn(personOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/persons/dogbreeders")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].firstName", is("Eva")))
                .andExpect(MockMvcResultMatchers.
                                jsonPath("$.[0].dogs.[0].name", is("Saar"))
                );
    }

    @Test
    void createPerson() throws Exception {
        when(personService.createPerson(personInputDto)).thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "firstName": "Hallo2",
                                            "lastName": "van de Merwe",
                                            "dateOfBirth": "2017-01-13",
                                            "sex": "female",
                                            "age": 36,
                                            "street": "Maas",
                                            "houseNumber": 31,
                                            "houseNumberExtension": null,
                                            "zipCode": "5172CN",
                                            "city": "Kaatsheuvel",
                                            "country": "the Netherlands",
                                            "dogs": [
                                                    {
                                                        "id": 1
                                                    }
                                                ]
                                        }""")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    void createPersonWithBindingResultError() throws Exception {
        when(personService.createPerson(personInputDto)).thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/persons")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePerson() throws Exception {
        when(personService.updatePerson(1L, personInputDto))
                .thenReturn(personOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/persons/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "firstName": "Hallo2",
                                            "lastName": "van de Merwe",
                                            "dateOfBirth": "2017-01-13",
                                            "sex": "female",
                                            "age": 36,
                                            "street": "Maas",
                                            "houseNumber": 31,
                                            "houseNumberExtension": null,
                                            "zipCode": "5172CN",
                                            "city": "Kaatsheuvel",
                                            "country": "the Netherlands",
                                            "dogs": [
                                                    {
                                                        "id": 1003
                                                    }
                                                ]
                                        }""")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void updatePersonWithBindingResultError() throws Exception {
        when(personService.updatePerson(1L, personInputDto))
                .thenReturn(personOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/persons/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchPerson() throws Exception {
        when(personService.patchPerson(1L, personPatchDto)).thenReturn(personOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/persons/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                              "firstName": "Eefjuh",
                                              "lastName": "van de Merwe",
                                              "sex": "female",
                                              "age": 36,
                                              "street": "Maas",
                                              "houseNumber": 31,
                                              "houseNumberExtension": null,
                                              "zipCode": "5172CN",
                                              "city": "Kaatsheuvel",
                                              "country": "the Netherlands"
                                        }""".indent(2))
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }
    @Test
    void patchPersonWithWrongInput() throws Exception{
        when(personService.patchPerson(1L, personPatchDto))
                .thenReturn(personOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/persons/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"test\": \"test\"\\}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePerson() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/persons/{id}", "1")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }
}