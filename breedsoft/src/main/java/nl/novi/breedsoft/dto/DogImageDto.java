package nl.novi.breedsoft.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DogImageDto {

    private Long id;

    @NotEmpty(message = "Please enter image name")
    private String imageName;

    private String imageType;

    @NotEmpty(message = "Please enter the image bytes")
    private byte[] dogImage;
}
