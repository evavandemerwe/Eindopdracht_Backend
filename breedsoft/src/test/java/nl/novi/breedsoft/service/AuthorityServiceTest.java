package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.authorityDtos.AuthorityInputDto;
import nl.novi.breedsoft.dto.authorityDtos.AuthorityOutputDto;
import nl.novi.breedsoft.exception.DuplicateNotAllowedException;
import nl.novi.breedsoft.exception.RecordNotFoundException;
import nl.novi.breedsoft.model.authority.Authority;
import nl.novi.breedsoft.repository.AuthorityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
        // Arrange
        Long expectedAuthorityID = 42L;
        Authority authority = new Authority();
        authority.setId(expectedAuthorityID);
        AuthorityInputDto authorityInputDto = new AuthorityInputDto();
        authorityInputDto.setAuthority("authority");
        when(authorityRepository.save(Mockito.any(Authority.class)))
                .thenReturn(authority);
        // Act
        Long result = authorityService.createAuthority(authorityInputDto);
        // Assert
        assertEquals(expectedAuthorityID, result);
    }

    @Test
    void createAuthorityDuplicateAuthority() {
        // Arrange
        AuthorityInputDto authorityInputDto = new AuthorityInputDto();
        authorityInputDto.setAuthority("authority");
        when(authorityRepository.findByAuthority(Mockito.anyString()))
                .thenReturn(Optional.of(new Authority()));
        // Act and Assert
        assertThrows(
                DuplicateNotAllowedException.class,
                () -> authorityService.createAuthority(authorityInputDto),
                "Expected duplicate authority exception to be thrown"
        );
    }

    @Test
    void deleteAuthority() {
        // Arrange
        Long expectedAuthorityID = 42L;
        when(authorityRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(new Authority()));
        // Act
        authorityService.deleteAuthority(expectedAuthorityID);
        // Assert
        verify(authorityRepository, times(1)).deleteById(expectedAuthorityID);
    }

    @Test
    void deleteAuthorityNoDelete() {
        // Arrange
        when(authorityRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(
                RecordNotFoundException.class,
                () -> authorityService.deleteAuthority(42L),
                "Expected record not found exception to be thrown"
        );
    }
}