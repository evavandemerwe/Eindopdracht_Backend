package nl.novi.breedsoft.dto.userDtos;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import nl.novi.breedsoft.model.authority.Authority;

import java.util.List;

@Data
public class UserOutputDto {
    // User output DTO doesn't return a password because this is secret information
    private Long id;

    @NotEmpty
    private String username;

    @JsonIncludeProperties("id")
    @NotEmpty
    private List<Authority> authorities;
}