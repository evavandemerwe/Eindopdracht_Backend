package nl.novi.breedsoft.utility;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

public class createUriResponse {

    /**
     * A method that creates an uri based on id and path
     * @param createdId the ID of the created entity
     * @param pathName name of the path on which the new entity is found
     * @return uri to the newly created entity
     */
    public static URI createUri(Long createdId, String pathName) {
        return URI.create(
                 ServletUriComponentsBuilder
                         .fromCurrentContextPath()
                         .path(pathName + createdId).toUriString());
    }

}
