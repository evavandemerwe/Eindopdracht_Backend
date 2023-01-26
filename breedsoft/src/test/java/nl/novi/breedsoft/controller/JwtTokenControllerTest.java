package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.security.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebMvcTest(JwtTokenController.class)
class JwtTokenControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    Authentication authentication;

    @MockBean
    TokenService tokenService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tear(){
        context = null;
        mockMvc = null;
        authentication = null;
        tokenService = null;
    }

    @Test
    void token() throws Exception {
        String jwt = "eyJraWQiOiJhNmFiMzllYy1lZTdjLTQzY2QtOTRjNy0zNGQwMjAzZTUwNWQiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJCcmVlZGVyMDEiLCJzdWIiOiJCcmVlZGVyMDEiLCJleHAiOjE2NzQzMTc2ODksImlhdCI6MTY3NDMxNDA4OSwicm9sZXMiOlsiUk9MRV9BRE1JTiJdfQ.kfjvKXhx3g2dY9B46_MN2Ex2mzcguhNHAyCC-G0AdzhgHtlJx6nCs3vDZI5UQjqIypGKB1QUobwsWJlRQLIglkIYtOtvI3QhIdHD7bG3QiWQ2PYePnfAQBdf_imceBykfpMkXI3OnUo4lUcHvfCntbX4IJ_U3IBca0IEvuc2JMhnmAo2cS9VLLV3lJzotYO92F5IMx4hMu3aV_YeXXluBp1UtLuiEym2ln5ygw_qO5VI8Dot9oIm7reQRfWFJr8tE5L6Hyr6LAEGzISN1aSVRjgJSHzTUURCh7awegXYhP4-lcpgP_ZZd6JEzWWuYoVKwboJ7Bhj1pkXNdqOkjN2cw";
        when(tokenService.generateToken(authentication)).thenReturn(jwt);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/token")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("Basic QnJlZWRlcjAxOkQwZ0JyMzNkM3IyMDIy")
                            .with(jwt())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}