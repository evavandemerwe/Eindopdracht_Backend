package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.userDtos.UserInputDto;
import nl.novi.breedsoft.dto.userDtos.UserOutputDto;
import nl.novi.breedsoft.dto.userDtos.UserPatchDto;
import nl.novi.breedsoft.model.authority.Authority;
import nl.novi.breedsoft.service.UserService;
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
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    List<UserOutputDto> userOutputDtoList = new ArrayList();
    UserOutputDto userOutputDto = new UserOutputDto();
    UserInputDto userInputDto = new UserInputDto();
    UserPatchDto userPatchDto = new UserPatchDto();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Authority authority = new Authority();
        List<Authority> authorityList = new ArrayList();
        authority.setAuthority("ROLE_ADMIN");
        authorityList.add(authority);

        userOutputDto.setAuthorities(authorityList);
        userOutputDto.setUsername("Eva");
        userOutputDtoList.add(userOutputDto);

        userInputDto.setAuthorities(authorityList);
        userInputDto.setUsername("Eva");

        userPatchDto.setAuthorities(authorityList);
        userPatchDto.setUsername("Eva");
    }

    @AfterEach
    void tear() {
        context = null;
        mockMvc = null;
        userService = null;
        userOutputDto = null;
        userOutputDtoList = null;
    }


    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(userOutputDtoList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/users")
                        .with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.[0].username", is("Eva")));
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(userInputDto))
                .thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("    {\n" +
                                        "        \"username\": \"User03\",\n" +
                                        "        \"password\": \"Password123!\",\n" +
                                        "        \"authorities\": [\n" +
                                        "            {\n" +
                                        "                \"id\": 1002\n" +
                                        "            }\n" +
                                        "        ]\n" +
                                        "    }")
                                .with(jwt())
                )
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isCreated());
    }

    @Test
    void createUserWithBindingResultError() throws Exception {
        when(userService.createUser(userInputDto))
                .thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchUser() throws Exception {
        when(userService.patchUser(1L, userPatchDto))
                .thenReturn(userOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.patch("/users/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("    {\n" +
                                        "        \"username\": \"!\",\n" +
                                        "        \"password\": \"Hoi1234!\",\n" +
                                        "        \"authorities\": [\n" +
                                        "            {\n" +
                                        "                \"id\": 1001\n" +
                                        "            }\n" +
                                        "        ]\n" +
                                        "    }")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void patchUserWithWrongInput() throws Exception {
        when(userService.patchUser(1L, userPatchDto))
                .thenReturn(userOutputDto);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"test\": \"test\"\\}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser() throws Exception {
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/users/{id}", "1")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent());
    }
}