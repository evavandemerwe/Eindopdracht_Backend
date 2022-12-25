package nl.novi.breedsoft.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import nl.novi.breedsoft.model.authority.Authority;

import java.util.Set;

@Data
public class UserOutputDto {
    // User output DTO only returns a username because password will be stored encrypted
    @NotEmpty
    private String username;
    @NotEmpty
    private Set<Authority> authorities;
}