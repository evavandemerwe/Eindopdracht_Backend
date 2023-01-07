package nl.novi.breedsoft.service;

import nl.novi.breedsoft.model.authority.User;
import nl.novi.breedsoft.repository.UserRepository;
import nl.novi.breedsoft.security.BreedSoftUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BreedSoftUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findByUsername(username);

        if(!foundUser.isPresent()) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        User user = foundUser.get();
        if(user == null || !user.getUsername().equals(username)) {
            throw new UsernameNotFoundException("Invalid credentials");
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getAuthorities().stream().forEach(
                authority -> {
                    grantedAuthorities.add(new SimpleGrantedAuthority(authority.getAuthority()));
                }
        );
        BreedSoftUser breedSoftUser = new BreedSoftUser(
                user.getUsername(),
                user.getPassword(),
                grantedAuthorities
        );
        return breedSoftUser;
    }
}
