package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.service.DomesticatedDogService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebMvcTest(DomesticatedDogController.class)
class DomesticatedDogControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DomesticatedDogService domesticatedDogService;

    DomesticatedDogOutputDto domesticatedDogOutputDto = new DomesticatedDogOutputDto();
    DomesticatedDogInputDto domesticatedDogInputDto = new DomesticatedDogInputDto();
    List<DomesticatedDogOutputDto> domesticatedDogOutputDtoList = new ArrayList();
    List<DomesticatedDogInputDto> domesticatedDogInputDtoList = new ArrayList();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        domesticatedDogOutputDto.setName("Saar");
        domesticatedDogOutputDto.setBreed(Breed.Dachschund);
        domesticatedDogOutputDto.setFood("dog chow");
        domesticatedDogOutputDtoList.add(domesticatedDogOutputDto);

        domesticatedDogInputDto.setName("Lotje");
        domesticatedDogOutputDto.setBreed(Breed.Dachschund);
        domesticatedDogOutputDto.setFood("dog chow");
        domesticatedDogInputDtoList.add(domesticatedDogInputDto);
    }

    @AfterEach
    void tear(){
        context = null;
        mockMvc = null;
        domesticatedDogService = null;
        domesticatedDogOutputDtoList = null;
    }

    @Test
    void getAllDomesticatedDogs() throws Exception {
        when(domesticatedDogService.getAllDomesticatedDogs()).thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));

    }

    @Test
    void getDomesticatedDogById() throws Exception {
        when(domesticatedDogService.getDomesticatedDogById(1L)).thenReturn(domesticatedDogOutputDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/id/{id}", "1").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Saar")));
    }

    @Test
    void getDomesticatedDogByName() throws Exception {
        when(domesticatedDogService.getDomesticatedDogByName("Saar")).thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/name/{name}", "Saar").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));
    }

    @Test
    void getChildrenById() throws Exception {
            when(domesticatedDogService.getAllChildren(1L)).thenReturn(domesticatedDogOutputDtoList);
            this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/dogs/{id}/children", "1").with(jwt()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));
        }

    @Test
    void getParentById() throws Exception {
        when(domesticatedDogService.getParentDog(1L)).thenReturn(domesticatedDogOutputDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/{id}/parent", "1").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Saar")));
    }

    @Test
    void getAvailableDogs() throws Exception {
        when(domesticatedDogService.getAvailableDomesticatedDogs()).thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/available").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));
    }

    @Test
    void getAllBreedDogs() throws Exception {
        when(domesticatedDogService.getDomesticatedBreedDogs()).thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/breeddogs").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));
    }

    @Test
    void createDomesticatedDog() throws Exception {
        when(domesticatedDogService.createDomesticatedDog(domesticatedDogInputDto)).thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/dogs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"color\": \"brown\",\n" +
                                        "    \"dateOfBirth\": \"2017-01-13\",\n" +
                                        "    \"dateOfDeath\": null,\n" +
                                        "    \"food\": \"dog chow\",\n" +
                                        "    \"sex\": \"male\",\n" +
                                        "    \"weightInGrams\": 5.0,\n" +
                                        "    \"kindOfHair\": \"short haired\",\n" +
                                        "    \"litter\": null,\n" +
                                        "    \"name\": \"hondje\",\n" +
                                        "    \"chipNumber\": 999999999999999,\n" +
                                        "    \"breed\": \"Dachschund\",\n" +
                                        "    \"breedGroup\": \"Hound\",\n" +
                                        "    \"hairColor\": \"Grey\",\n" +
                                        "    \"person\": {\n" +
                                        "        \"id\": 2001\n" +
                                        "    },\n" +
                                        "    \"dogStatus\": \"ownedDog\"\n" +
                                        "}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    void createDomesticatedDogWithBindingResultError() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/dogs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadLitter() throws Exception {
        when(domesticatedDogService.createLitterList(domesticatedDogInputDtoList, 1L)).thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/dogs/{id}/children", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("[\n" +
                                        "    {\n" +
                                        "    \"color\": \"brown\",\n" +
                                        "    \"dateOfBirth\": \"2017-01-13\",\n" +
                                        "    \"dateOfDeath\": null,\n" +
                                        "    \"food\": \"dog chow\",\n" +
                                        "    \"sex\": \"male\",\n" +
                                        "    \"weightInGrams\": 5.0,\n" +
                                        "    \"kindOfHair\": \"short haired\",\n" +
                                        "    \"litter\": null,\n" +
                                        "    \"name\": \"hondje\",\n" +
                                        "    \"chipNumber\": 999999999999999,\n" +
                                        "    \"breed\": \"Dachschund\",\n" +
                                        "    \"breedGroup\": \"Hound\",\n" +
                                        "    \"hairColor\": \"Grey\",\n" +
                                        "    \"canSee\": true,\n" +
                                        "    \"canHear\": true\n" +
                                        "    },\n" +
                                        "    {\n" +
                                        "    \"color\": \"brown\",\n" +
                                        "    \"dateOfBirth\": \"2017-01-13\",\n" +
                                        "    \"dateOfDeath\": null,\n" +
                                        "    \"food\": \"dog chow\",\n" +
                                        "    \"sex\": \"female\",\n" +
                                        "    \"weightInGrams\": 5.0,\n" +
                                        "    \"kindOfHair\": \"short haired\",\n" +
                                        "    \"litter\": null,\n" +
                                        "    \"name\": \"hondje\",\n" +
                                        "    \"chipNumber\": 999999999999999,\n" +
                                        "    \"breed\": \"Dachschund\",\n" +
                                        "    \"breedGroup\": \"Hound\",\n" +
                                        "    \"hairColor\": \"Grey\",\n" +
                                        "    \"canSee\": false,\n" +
                                        "    \"canHear\": true\n" +
                                        "    }\n" +
                                        "]")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void uploadLitterWithBindingResultError() throws Exception {
         this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/dogs/{id}/children", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadDogImage() throws Exception {
        Resource fileResource = new ClassPathResource(".\\images\\Lotje.jpeg");
        MockMultipartFile image = new MockMultipartFile("image", fileResource.getFilename(), MediaType.MULTIPART_FORM_DATA_VALUE, fileResource.getInputStream());
        when(domesticatedDogService.storeDogImage(1L, image)).thenReturn(1L);

        this.mockMvc.perform(
            MockMvcRequestBuilders.multipart("/dogs/{id}/image", "1")
            .file(image)
            .with(jwt())
        )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isCreated());
    }

    @Test
    void uploadDogImageWithBindingResultError() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/dogs/{id}/image", "1")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDog() {
    }

    @Test
    void patchDomesticatedDog() {
    }

    @Test
    void deleteDogImage() {
    }

    @Test
    void deleteDog() {
    }
}