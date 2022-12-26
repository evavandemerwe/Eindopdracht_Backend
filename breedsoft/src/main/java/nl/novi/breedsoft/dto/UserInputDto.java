package nl.novi.breedsoft.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import nl.novi.breedsoft.model.authority.Authority;

import java.util.List;

@Data
public class UserInputDto {

    @NotEmpty
    private String username;
    @NotEmpty
    @Pattern(
            regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}",
            message = "Password must contain at least one digit, one uppercase character, one lowercase character, a special character and must be between 8 and character long"
    )
    private String password;

    @NotEmpty
    @JsonIncludeProperties("id")
    private List<Authority> authorities;
}
