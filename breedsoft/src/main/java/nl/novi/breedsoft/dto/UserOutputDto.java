package nl.novi.breedsoft.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import nl.novi.breedsoft.model.authority.Authority;

import java.util.List;

@Data
public class UserOutputDto {
    // User output DTO only returns a username because password will be stored encrypted
    @NotEmpty
    private String username;

    @JsonIncludeProperties("id")
    @NotEmpty
    private List<Authority> authorities;
}