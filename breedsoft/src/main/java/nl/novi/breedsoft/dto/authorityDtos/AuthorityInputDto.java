package nl.novi.breedsoft.dto.authorityDtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthorityInputDto {
    @NotEmpty
    private String authority;
}
