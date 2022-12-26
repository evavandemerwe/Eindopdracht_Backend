package nl.novi.breedsoft.dto;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.Data;
import nl.novi.breedsoft.model.authority.Authority;

import java.util.List;

@Data
public class UserPatchDto {
    private Long id;
    private String username;
    private String password;
    @JsonIncludeProperties("id")
    private List<Authority> authorities;

}
