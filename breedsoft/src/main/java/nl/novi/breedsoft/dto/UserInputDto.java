package nl.novi.breedsoft.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class UserInputDto {

    @NotEmpty
    private String username;
    @NotEmpty
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}",
            message = "Password must contain at least one digit, one uppercase character, one lowercase character, a special character and must be between 8 and character long"
    )
    private String password;
}
