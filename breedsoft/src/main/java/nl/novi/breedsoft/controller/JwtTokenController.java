package nl.novi.breedsoft.controller;

import nl.novi.breedsoft.security.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtTokenController {

    private final TokenService tokenService;

    public JwtTokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * POST method to get a token if the correct username password combination is provided
     * @param authentication to validate the request
     * @return jwt token and log statements for check
     */
    @PostMapping("/token")
    public String token(Authentication authentication) {
        String token = tokenService.generateToken(authentication);
        return token;
    }
}
