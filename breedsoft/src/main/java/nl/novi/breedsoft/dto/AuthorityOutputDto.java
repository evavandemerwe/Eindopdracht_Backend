package nl.novi.breedsoft.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthorityOutputDto {

    @NotEmpty
    private Long id;
    @NotEmpty
    private String authority;

    public Long getId() {
        return (id != null) ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
