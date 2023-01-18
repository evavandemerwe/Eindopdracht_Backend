package nl.novi.breedsoft.utility;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

public class createUriResponse {

    public static URI createUri(Long createdId, String pathName) {
       URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path(pathName + createdId).toUriString());
       return uri;
    }

}
