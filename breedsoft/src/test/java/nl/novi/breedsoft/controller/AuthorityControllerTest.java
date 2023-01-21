package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.dto.authorityDtos.AuthorityInputDto;
import nl.novi.breedsoft.dto.authorityDtos.AuthorityOutputDto;
import nl.novi.breedsoft.service.AuthorityService;
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
@WebMvcTest(AuthorityController.class)
class AuthorityControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthorityService authorityService;

    List<AuthorityOutputDto> authorityList = new ArrayList<>();

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        AuthorityOutputDto authority = new AuthorityOutputDto();
        authority.setAuthority("ROLE_TESTER");
        authorityList.add(authority);
    }

    @AfterEach
    void tear(){
        context = null;
        mockMvc = null;
        authorityService = null;
        authorityList = null;
    }

    @Test
    void getAllAuthorities() throws Exception {
        when(authorityService.getAllAuthorities()).thenReturn(authorityList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/authorities").with(jwt()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].authority", is("ROLE_TESTER")));
    }

    @Test
    void getAllAuthoritiesWithoutJwt() throws Exception {
        when(authorityService.getAllAuthorities()).thenReturn(authorityList);
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/authorities"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    void createAuthority() throws Exception {

        AuthorityInputDto authorityInputDto = new AuthorityInputDto();
        authorityInputDto.setAuthority("ROLE_TESTER");

        when(authorityService.createAuthority(authorityInputDto)).thenReturn(1L);
        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/authorities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"authority\": \"ROLE_TESTER\" }")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }
    @Test
    void createAuthorityWithBindingResultError() throws Exception {

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/authorities")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}")
                                .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());

    }
    @Test
    void deleteAuthority() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/authorities/{id}", "1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
    }

}