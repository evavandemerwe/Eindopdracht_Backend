package nl.novi.breedsoft.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class BreedSoftUser extends User {
    /**
     * Create a BreedSoft user with given username, password and granted authorities.
     * Extension of the Spring Security framework user authentication and authorization information
     * @param username Username of the user to be created
     * @param password Password of the user to be created
     * @param authorities Authorities of the user to be created
     */
    public BreedSoftUser(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
    }
}
