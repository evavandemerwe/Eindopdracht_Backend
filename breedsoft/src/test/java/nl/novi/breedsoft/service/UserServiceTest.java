package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.userDtos.UserInputDto;
import nl.novi.breedsoft.dto.userDtos.UserOutputDto;
import nl.novi.breedsoft.dto.userDtos.UserPatchDto;
import nl.novi.breedsoft.model.authority.User;
import nl.novi.breedsoft.repository.AuthorityRepository;
import nl.novi.breedsoft.repository.PersonRepository;
import nl.novi.breedsoft.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AuthorityRepository authorityRepository;

    @Mock
    PasswordEncoder encoder;

    @Mock
    PersonRepository personRepository;

    UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(
            userRepository,
            authorityRepository,
            encoder,
            personRepository
        );
    }

    @Test
    void getAllUsers() {
        // Arrange
        User user = new User();
        String expectedName = "expectedName";
        user.setUsername(expectedName);
        List<User> users = new ArrayList<>(List.of(user));
        when(userRepository.findAll()).thenReturn(users);
        // Act
        List<UserOutputDto> result = userService.getAllUsers();
        // Assert
        assertEquals(1, result.size());
        assertEquals(expectedName, result.get(0).getUsername());
    }

    @Test
    void createUser() {
        // Arrange
        UserInputDto userInputDto = new UserInputDto();
        String expectedUsername = "expectedUsername";
        userInputDto.setUsername(expectedUsername);
        String expectedPassword = "expectedPassword";
        userInputDto.setPassword(expectedPassword);
        userInputDto.setAuthorities(new ArrayList<>());
        when(encoder.encode(Mockito.anyString())).thenAnswer(i -> i.getArguments()[0]);
        final AtomicReference<User> userAtomicReference = new AtomicReference<>() ;
        when(userRepository.save(Mockito.any(User.class))).then(
                i -> {
                    userAtomicReference.set((User)i.getArguments()[0]);
                    return i.getArguments()[0];
                }
        );
        // Act
        userService.createUser(userInputDto);
        // Assert
        User storedMedicalData = userAtomicReference.get();
        assertEquals(expectedUsername, storedMedicalData.getUsername());
        assertEquals(expectedPassword, storedMedicalData.getPassword());
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void patchUser() {
        // Arrange
        UserPatchDto userPatchDto = new UserPatchDto();
        String expectedUsername = "expectedUsername";
        userPatchDto.setUsername(expectedUsername);
        User user = new User();
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(userRepository.getReferenceById(Mockito.anyLong())).thenReturn(user);
        // Act
        UserOutputDto result = userService.patchUser(42L, userPatchDto);
        // Assert
        verify(userRepository, times(1)).save(user);
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedUsername, result.getUsername());
    }

    @Test
    void deleteUser() {
        // Arrange
        Long id = 42L;
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new User()));
        // Act
        userService.deleteUser(id);
        // Assert
        verify(userRepository, times(1)).deleteById(id);
    }
}