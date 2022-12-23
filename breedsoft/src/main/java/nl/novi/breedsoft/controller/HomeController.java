package nl.novi.breedsoft.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home(Authentication authentication) {
        return "Username: " + authentication.getName();
    }

}
