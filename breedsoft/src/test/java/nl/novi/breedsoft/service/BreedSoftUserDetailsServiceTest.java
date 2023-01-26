package nl.novi.breedsoft.service;

import nl.novi.breedsoft.model.authority.Authority;
import nl.novi.breedsoft.model.authority.User;
import nl.novi.breedsoft.repository.UserRepository;
import nl.novi.breedsoft.security.BreedSoftUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BreedSoftUserDetailsServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    BreedSoftUserDetailsService breedSoftUserDetailsService;

    @AfterEach
    void tearDown() {
        Mockito.reset(userRepository);
    }

    @Test
    void loadUserByUsername() {
        // Arrange
        String expectedPassword = "expectedPassword";
        String expectedUsername = "expectedUsername";
        String expectedAuthority = "expectedAuthority";
        Authority authority = new Authority();
        authority.setAuthority(expectedAuthority);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authority);
        User user = new User(42L, expectedUsername, expectedPassword, null, authorities);
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        // Act
        UserDetails result = this.breedSoftUserDetailsService.loadUserByUsername(expectedUsername);
        // Assert
        assertInstanceOf(BreedSoftUser.class, result);
        assertEquals(expectedUsername, result.getUsername());
        assertEquals(expectedPassword, result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        assertEquals(expectedAuthority, result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsernameWithNoUser() {
        // Arrange
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> this.breedSoftUserDetailsService.loadUserByUsername("username"),
                "Expected username not found exception to be thrown"
        );
    }

    @Test
    void loadUserByUsernameWithNotEqualUsername() {
        // Arrange
        User user = new User(42L, "", "", null, null);
        when(userRepository.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        // Act and Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> this.breedSoftUserDetailsService.loadUserByUsername("username"),
                "Expected username not found exception to be thrown"
        );
    }
}