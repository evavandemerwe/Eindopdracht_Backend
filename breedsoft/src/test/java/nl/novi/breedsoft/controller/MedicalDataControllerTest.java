package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.medicalDataDtos.MedicalDataOutputDto;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.service.MedicalDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.util.Calendar.MONTH;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ContextConfiguration
@WebMvcTest(MedicalDataController.class)
class MedicalDataControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MedicalDataService medicalDataService;


    MedicalDataOutputDto medicalDataOutputDto;
    List<MedicalDataOutputDto> medicalDataOutputDtoList;

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
    }

    @AfterEach
    void tear() {
        context = null;
        mockMvc = null;
        medicalDataService = null;
    }

    @Test
    void getAllMedicalData() throws Exception {
       // when(medicalDataService.getAllMedicalData()).thenReturn();

    }

    @Test
    void getMedicalDataById() {
    }

    @Test
    void createMedicalData() {
    }

    @Test
    void updateMedicalData() {
    }

    @Test
    void patchMedicalData() {
    }

    @Test
    void deleteMedicalData() {
    }
}