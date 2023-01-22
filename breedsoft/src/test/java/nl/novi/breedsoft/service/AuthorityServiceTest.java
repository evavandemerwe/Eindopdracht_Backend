package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.authorityDtos.AuthorityOutputDto;
import nl.novi.breedsoft.model.authority.Authority;
import nl.novi.breedsoft.repository.AuthorityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorityServiceTest {

    @Mock
    AuthorityRepository authorityRepository;

    AuthorityService authorityService;
    @BeforeEach
    void setUp() {
        this.authorityService = new AuthorityService(
                this.authorityRepository
        );
    }

    @Test
    void getAllAuthorities() {
        // Arrange
        String expectedAuthority = "authority";
        Authority authority = new Authority();
        authority.setAuthority(expectedAuthority);
        List<Authority> expectedAuthorities = new ArrayList<>();
        expectedAuthorities.add(authority);
        when(authorityRepository.findAll()).thenReturn(expectedAuthorities);
        // Act
        List<AuthorityOutputDto> result = authorityService.getAllAuthorities();
        // Assert
        assertEquals(expectedAuthorities.size(), result.size());
        assertEquals(expectedAuthority, result.get(0).getAuthority());
    }

    @Test
    void getAllAuthoritiesEmptyRepositoryResponse() {
        // Arrange
        when(authorityRepository.findAll()).thenReturn(new ArrayList<>());
        // Act
        List<AuthorityOutputDto> result = authorityService.getAllAuthorities();
        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void createAuthority() {
    }

    @Test
    void deleteAuthority() {
    }
}