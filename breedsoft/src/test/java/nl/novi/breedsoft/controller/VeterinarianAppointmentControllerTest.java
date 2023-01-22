package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.domesticatedDogDtos.DomesticatedDogOutputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentInputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentOutputDto;
import nl.novi.breedsoft.dto.veterinarianAppointmentDtos.VeterinarianAppointmentPatchDto;
import nl.novi.breedsoft.exception.EnumValueNotFoundException;
import nl.novi.breedsoft.model.management.DomesticatedDog;
import nl.novi.breedsoft.model.management.enumerations.Breed;
import nl.novi.breedsoft.service.VeterinarianAppointmentService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebMvcTest(VeterinarianAppointmentController.class)
class VeterinarianAppointmentControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VeterinarianAppointmentService veterinarianAppointmentService;

    VeterinarianAppointmentOutputDto veterinarianAppointmentOutputDto = new VeterinarianAppointmentOutputDto();
    VeterinarianAppointmentInputDto veterinarianAppointmentInputDto = new VeterinarianAppointmentInputDto();
    VeterinarianAppointmentPatchDto veterinarianAppointmentPatchDto = new VeterinarianAppointmentPatchDto();
    List<VeterinarianAppointmentOutputDto> veterinarianAppointmentOutputDtoList = new ArrayList();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        DomesticatedDogOutputDto domesticatedDogOutputDto = new DomesticatedDogOutputDto();
        domesticatedDogOutputDto.setName("Saar");
        domesticatedDogOutputDto.setBreed(Breed.Dachschund);
        domesticatedDogOutputDto.setFood("dog chow");

        veterinarianAppointmentOutputDto.setAppointmentDate(LocalDate.of(2023, Month.JANUARY, 13));
        veterinarianAppointmentOutputDto.setDomesticatedDog(domesticatedDogOutputDto);
        veterinarianAppointmentOutputDto.setSubject("Check up");
        veterinarianAppointmentOutputDtoList.add(veterinarianAppointmentOutputDto);
    }

    @AfterEach
    void tear(){
        context = null;
        mockMvc = null;
        veterinarianAppointmentService = null;
        veterinarianAppointmentInputDto = null;
        veterinarianAppointmentOutputDto = null;
        veterinarianAppointmentPatchDto = null;
        veterinarianAppointmentOutputDtoList = null;
    }

    @Test
    void getAllAppointments() throws Exception {
        when(veterinarianAppointmentService.getAllAppointments())
                .thenReturn(veterinarianAppointmentOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/appointments")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].appointmentDate", is("2023-01-13")));
    }

    @Test
    void getAllAppointmentsForDogId() throws Exception {
        when(veterinarianAppointmentService.getAllAppointmentsByDogId(1L))
                .thenReturn(veterinarianAppointmentOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/appointments/dogid/{id}", "1")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                jsonPath("$.[0].subject", is("Check up")));
    }

    @Test
    void createAppointment() throws Exception {
        when(veterinarianAppointmentService.createVeterinarianAppointment(veterinarianAppointmentInputDto))
                .thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "        \"appointmentDate\": \"2023-02-23\",\n" +
                                        "        \"subject\": \"Second ultrasound examination\",\n" +
                                        "        \"domesticatedDog\": {\n" +
                                        "            \"id\": \"1005\"\n" +
                                        "        }\n" +
                                        "    }")
                                .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    void createAppointmentWithBindingResultError() throws Exception {
        when(veterinarianAppointmentService.createVeterinarianAppointment(veterinarianAppointmentInputDto))
                .thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ }")
                                .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateVeterinarianAppointment() throws Exception {
        when(veterinarianAppointmentService.updateVeterinarianAppointment(1L, veterinarianAppointmentInputDto))
                .thenReturn(veterinarianAppointmentOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/appointments/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"appointmentDate\": \"2023-02-23\",\n" +
                                        "    \"subject\": \"hii examination\",\n" +
                                        "    \"domesticatedDog\": {\n" +
                                        "        \"id\": 1005\n" +
                                        "    }\n" +
                                        "}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void updateVeterinarianAppointmentWithBindingResultError() throws Exception {
        when(veterinarianAppointmentService.updateVeterinarianAppointment(1L, veterinarianAppointmentInputDto))
                .thenReturn(veterinarianAppointmentOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.put("/appointments/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchVeterinarianAppointment() throws Exception {
        when(veterinarianAppointmentService.patchVeterinarianAppointment(1L, veterinarianAppointmentPatchDto))
                .thenReturn(veterinarianAppointmentOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/appointments/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"appointmentDate\": \"2023-02-23\",\n" +
                                        "    \"subject\": \"hii examination\",\n" +
                                        "    \"domesticatedDog\": {\n" +
                                        "        \"id\": 1005\n" +
                                        "    }\n" +
                                        "}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void patchVeterinarianAppointmentWithWrongInput() throws Exception {
        when(veterinarianAppointmentService.patchVeterinarianAppointment(1L, veterinarianAppointmentPatchDto))
                .thenReturn(veterinarianAppointmentOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/appointments/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"test\": \"test\"\\}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAppointment() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/appointments/{id}", "1")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }
}