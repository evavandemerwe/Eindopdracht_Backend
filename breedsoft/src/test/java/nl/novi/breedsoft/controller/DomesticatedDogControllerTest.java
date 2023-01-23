package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogInputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogPatchDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
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
import java.io.IOException;
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
    DomesticatedDogPatchDto domesticatedDogPatchDto = new DomesticatedDogPatchDto();
    DomesticatedDogPatchDto domesticatedDogPatchDtoWithWrongEnum = new DomesticatedDogPatchDto();
    List<DomesticatedDogOutputDto> domesticatedDogOutputDtoList = new ArrayList<>();
    List<DomesticatedDogInputDto> domesticatedDogInputDtoList = new ArrayList<>();

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

        domesticatedDogPatchDto.setName("Lotje");
        domesticatedDogPatchDto.setBreed("");
        domesticatedDogPatchDto.setFood("dog chow");

        domesticatedDogPatchDtoWithWrongEnum.setName("Lotje");
        domesticatedDogPatchDtoWithWrongEnum.setSex("x");
        domesticatedDogPatchDtoWithWrongEnum.setFood("dog chow");

    }

    @AfterEach
    void tear(){
        context = null;
        mockMvc = null;
        domesticatedDogService = null;
        domesticatedDogInputDto = null;
        domesticatedDogPatchDto = null;
        domesticatedDogOutputDto = null;
        domesticatedDogOutputDtoList = null;
        domesticatedDogInputDtoList = null;
    }

    @Test
    void getAllDomesticatedDogs() throws Exception {
        when(domesticatedDogService.getAllDomesticatedDogs())
                .thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].name", is("Saar")));
    }

    @Test
    void getDomesticatedDogById() throws Exception {
        when(domesticatedDogService.getDomesticatedDogById(1L))
                .thenReturn(domesticatedDogOutputDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/id/{id}", "1").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Saar")));
    }

    @Test
    void getDomesticatedDogByName() throws Exception {
        when(domesticatedDogService.getDomesticatedDogByName("Saar"))
                .thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/name/{name}", "Saar").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));
    }

    @Test
    void getChildrenById() throws Exception {
            when(domesticatedDogService.getAllChildren(1L))
                    .thenReturn(domesticatedDogOutputDtoList);
            this.mockMvc
                    .perform(MockMvcRequestBuilders.get("/dogs/{id}/children", "1").with(jwt()))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));
        }

    @Test
    void getParentById() throws Exception {
        when(domesticatedDogService.getParentDog(1L))
                .thenReturn(domesticatedDogOutputDto);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/{id}/parent", "1").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", is("Saar")));
    }

    @Test
    void getAvailableDogs() throws Exception {
        when(domesticatedDogService.getAvailableDomesticatedDogs())
                .thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/available").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));
    }

    @Test
    void getAllBreedDogs() throws Exception {
        when(domesticatedDogService.getDomesticatedBreedDogs())
                .thenReturn(domesticatedDogOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/dogs/breeddogs").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name", is("Saar")));
    }

    @Test
    void createDomesticatedDog() throws Exception {
        when(domesticatedDogService.createDomesticatedDog(domesticatedDogInputDto))
                .thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/dogs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "color": "brown",
                                            "dateOfBirth": "2017-01-13",
                                            "dateOfDeath": null,
                                            "food": "dog chow",
                                            "sex": "male",
                                            "weightInGrams": 5.0,
                                            "kindOfHair": "short haired",
                                            "litter": null,
                                            "name": "hondje",
                                            "chipNumber": 999999999999999,
                                            "breed": "Dachschund",
                                            "breedGroup": "Hound",
                                            "hairColor": "Grey",
                                            "person": {
                                                "id": 2001
                                            },
                                            "dogStatus": "ownedDog"
                                        }""")
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
                                .content("""
                                        [
                                            {
                                            "color": "brown",
                                            "dateOfBirth": "2017-01-13",
                                            "dateOfDeath": null,
                                            "food": "dog chow",
                                            "sex": "male",
                                            "weightInGrams": 5.0,
                                            "kindOfHair": "short haired",
                                            "litter": null,
                                            "name": "hondje",
                                            "chipNumber": 999999999999999,
                                            "breed": "Dachschund",
                                            "breedGroup": "Hound",
                                            "hairColor": "Grey",
                                            "canSee": true,
                                            "canHear": true
                                            },
                                            {
                                            "color": "brown",
                                            "dateOfBirth": "2017-01-13",
                                            "dateOfDeath": null,
                                            "food": "dog chow",
                                            "sex": "female",
                                            "weightInGrams": 5.0,
                                            "kindOfHair": "short haired",
                                            "litter": null,
                                            "name": "hondje",
                                            "chipNumber": 999999999999999,
                                            "breed": "Dachschund",
                                            "breedGroup": "Hound",
                                            "hairColor": "Grey",
                                            "canSee": false,
                                            "canHear": true
                                            }
                                        ]""")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void uploadLitterWithBindingResultError() throws Exception {
        when(domesticatedDogService.createLitterList(domesticatedDogInputDtoList, 1L))
                .thenReturn(domesticatedDogOutputDtoList);
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
        Resource fileResource = new ClassPathResource(".\\images\\Lotje.txt");
        MockMultipartFile image = new MockMultipartFile("image", fileResource.getFilename(), MediaType.MULTIPART_FORM_DATA_VALUE, fileResource.getInputStream());
        when(domesticatedDogService.storeDogImage(1L, image))
                .thenThrow(IOException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.multipart("/dogs/{id}/image", "1")
                                .file(image)
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    void updateDog() throws Exception {
        when(domesticatedDogService.updateDomesticatedDog(1L, domesticatedDogInputDto))
                .thenReturn(domesticatedDogOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/dogs/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                                "color": "brown",
                                                "dateOfBirth": "2017-01-13",
                                                "dateOfDeath": null,
                                                "food": "dog chow",
                                                "sex": "female",
                                                "weightInGrams": 5.0,
                                                "kindOfHair": "short haired",
                                                "litter": null,
                                                "name": "pupje",
                                                "chipNumber": 999999999999999,
                                                "breed": "Dachschund",
                                                "breedGroup": "Hound",
                                                "hairColor" : "Grey",
                                                "person": {
                                                    "id" : 2001
                                                },
                                                "dogStatus" : "availablePreOwned"
                                            }""")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void updateDogWithBindingResultError() throws Exception {
        when(domesticatedDogService.updateDomesticatedDog(1L, domesticatedDogInputDto))
                .thenReturn(domesticatedDogOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/dogs/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDomesticatedDogWithWrongEnumValue() throws Exception{
        when(domesticatedDogService.patchDomesticatedDog(1L, domesticatedDogPatchDtoWithWrongEnum))
                .thenThrow(EnumValueNotFoundException.class);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/dogs/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "color": "brown",
                                            "dateOfBirth": "2017-01-13",
                                            "dateOfDeath": null,
                                            "food": "dog chow",
                                            "sex": "x",
                                            "weightInGrams": 5.0,
                                            "kindOfHair": "short haired",
                                            "litter": null,
                                            "name": "pupje",
                                            "chipNumber": 999999999999999,
                                            "breed": "Dachschund",
                                            "breedGroup": "Hound",
                                            "hairColor" : "Grey",
                                            "person": {
                                                "id" : 2001
                                            },
                                            "dogStatus" : "availablePreOwned"
                                        }""".indent(4))
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchDomesticatedDog() throws Exception{
        when(domesticatedDogService.patchDomesticatedDog(1L, domesticatedDogPatchDto))
                .thenReturn("Your dog has been updated.");
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/dogs/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "name": "Hondje1",
                                            "hairColor": "yellow",
                                            "food": "Hamburgers",
                                            "sex": "female",
                                            "weightInGrams": 0.5,
                                            "kindOfHair": "long haired",
                                            "dogYears": 35,
                                            "dateOfBirth": "2018-01-13",
                                            "breed": "Dachschund",
                                            "person": {
                                                "id" : 2001
                                            }
                                            }""")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @Test
    void patchDomesticatedDogWithWrongInput() throws Exception{
        when(domesticatedDogService.patchDomesticatedDog(1L, domesticatedDogPatchDto))
                .thenReturn("Your dog has been updated.");
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/dogs/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"test\": \"test\"\\}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());

    }


    @Test
    void deleteDogImage() throws Exception{
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/dogs/{id}/image", "1")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());

    }

    @Test
    void deleteDog() throws Exception{
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/dogs/{id}", "1")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }
}