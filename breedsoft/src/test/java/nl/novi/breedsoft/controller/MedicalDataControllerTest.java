package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataInputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataOutputDto;
import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataPatchDto;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.service.MedicalDataService;
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

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebMvcTest(MedicalDataController.class)
class MedicalDataControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MedicalDataService medicalDataService;


    MedicalDataOutputDto medicalDataOutputDto = new MedicalDataOutputDto();
    MedicalDataInputDto medicalDataInputDto = new MedicalDataInputDto();
    MedicalDataPatchDto medicalDataPatchDto = new MedicalDataPatchDto();
    List<MedicalDataOutputDto> medicalDataOutputDtoList = new ArrayList();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        DomesticatedDog dog = new DomesticatedDog();
        dog.setName("Saar");
        dog.setBreed(Breed.Dachschund);
        dog.setFood("dog chow");
        medicalDataOutputDto.setDomesticatedDog(dog);
        medicalDataOutputDto.setTreatment("Check-up");
        medicalDataOutputDto.setDateOfMedicalTreatment(LocalDate.of(2023, Month.JANUARY, 13));
        medicalDataOutputDto.setDiagnose("Healthy");
        medicalDataOutputDto.setMedicine("None");
        medicalDataOutputDtoList.add(medicalDataOutputDto);

        medicalDataInputDto.setDomesticatedDog(dog);
        medicalDataInputDto.setTreatment("Check-up");
        medicalDataInputDto.setDateOfMedicalTreatment(LocalDate.of(2023, Month.JANUARY, 13));
        medicalDataInputDto.setDiagnose("Healthy");
        medicalDataInputDto.setMedicine("None");

        medicalDataPatchDto.setDomesticatedDog(dog);
        medicalDataPatchDto.setTreatment("Check-up");
        medicalDataPatchDto.setDateOfMedicalTreatment(LocalDate.of(2023, Month.JANUARY, 13));
        medicalDataPatchDto.setDiagnose("Healthy");
        medicalDataPatchDto.setMedicine("None");
    }

    @AfterEach
    void tear() {
        context = null;
        mockMvc = null;
        medicalDataService = null;
        medicalDataOutputDto = null;
        medicalDataOutputDtoList = null;
    }

    @Test
    void getAllMedicalData() throws Exception {
       when(medicalDataService.getAllMedicalData()).thenReturn(medicalDataOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/medicaldata").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].domesticatedDog.name", is("Saar")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].dateOfMedicalTreatment", is("2023-01-13")));
    }

    @Test
    void getMedicalDataByDomesticatedDogId() throws Exception{
        when(medicalDataService.getmedicaldatabydogid(1L))
                .thenReturn(medicalDataOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/medicaldata/id/{id}", "1").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].treatment", is("Check-up")));
    }

    @Test
    void createMedicalData() throws Exception{
        when(medicalDataService.createMedicalData(medicalDataInputDto))
                .thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/medicaldata")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("    {\n" +
                                        "        \"dateOfMedicalTreatment\": \"2022-01-10\",\n" +
                                        "        \"medicine\": \"Anti flea product dog\",\n" +
                                        "        \"diagnose\": \"Healty\",\n" +
                                        "        \"treatment\": \"Prescribed medicine\",\n" +
                                        "        \"domesticatedDog\": {\n" +
                                        "            \"id\": 1\n" +
                                        "        }\n" +
                                        "    }")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    void createMedicalDataWithBindingResultError() throws Exception{
        when(medicalDataService.createMedicalData(medicalDataInputDto))
                .thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/medicaldata")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateMedicalData() throws Exception {
        when(medicalDataService.updateMedicalData(1L, medicalDataInputDto))
                .thenReturn(medicalDataOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/medicaldata/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("    {\n" +
                                        "        \"dateOfMedicalTreatment\": \"2018-06-02\",\n" +
                                        "        \"medicine\": \"None\",\n" +
                                        "        \"diagnose\": \"Sick as a dog\",\n" +
                                        "        \"treatment\": \"Annual dog vaccination\",\n" +
                                        "        \"domesticatedDog\": {\n" +
                                        "            \"id\": 1002\n" +
                                        "        }\n" +
                                        "    }")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void updateMedicalDataWithBindingResultError() throws Exception {
        when(medicalDataService.updateMedicalData(1L, medicalDataInputDto))
                .thenReturn(medicalDataOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/medicaldata/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchMedicalData() throws Exception {
        when(medicalDataService.patchMedicalData(1L, medicalDataPatchDto))
                .thenReturn(medicalDataOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/medicaldata/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("    {\n" +
                                        "        \"dateOfMedicalTreatment\": \"2018-06-02\",\n" +
                                        "        \"medicine\": \"None\",\n" +
                                        "        \"diagnose\": \"Sick as a dog\",\n" +
                                        "        \"treatment\": \"Annual dog vaccination\",\n" +
                                        "        \"domesticatedDog\": {\n" +
                                        "            \"id\": 1002\n" +
                                        "        }\n" +
                                        "    }")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void patchMedicalDataWithWrongInput() throws Exception {
        when(medicalDataService.patchMedicalData(1L, medicalDataPatchDto))
                .thenReturn(medicalDataOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/medicaldata/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ \"name\": \"null\"\\}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchMedicalDataWithEmptyBody() throws Exception {
        when(medicalDataService.patchMedicalData(1L, medicalDataPatchDto)).thenReturn(medicalDataOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/medicaldata/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ }")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteMedicalData()  throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/medicaldata/{id}", "1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(jwt()))
                .andExpect(status().isNoContent());
    }
}