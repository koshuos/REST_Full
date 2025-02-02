package com.RESTfullApi;

import com.RESTfullApi.Entity.User;
import com.RESTfullApi.Service.UserService;
import com.RESTfullApi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById() {
        User user = new User("Oleg", "koshuos@gmail.com", LocalDate.of(1982, 8, 4), 1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<User> resultOpt = userService.getUserById(1L);

        // Assert
        assertTrue(resultOpt.isPresent(), "User should be present");
        User result = resultOpt.get();
        assertNotNull(result, "User should not be null");
        assertEquals("Oleg", result.getName());
        assertEquals("koshuos@gmail.com", result.getEmail());
        assertEquals(LocalDate.of(1982, 8, 4), result.getBirthDate());
        assertEquals(1L, result.getId());
    }
}
