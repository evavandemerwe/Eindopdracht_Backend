package nl.novi.breedsoft.service;

import nl.novi.breedsoft.dto.userDtos.UserOutputDto;
import nl.novi.breedsoft.model.authority.User;
import nl.novi.breedsoft.repository.AuthorityRepository;
import nl.novi.breedsoft.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AuthorityRepository authorityRepository;

    @Mock
    PasswordEncoder encoder;

    UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(
            userRepository,
            authorityRepository,
            encoder
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
    }

    @Test
    void patchUser() {
    }

    @Test
    void deleteUser() {
    }
}