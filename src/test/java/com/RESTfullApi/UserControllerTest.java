package com.RESTfullApi;

import com.RESTfullApi.Controller.UserController;
import com.RESTfullApi.Entity.User;
import com.RESTfullApi.Service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @TestConfiguration
    static class MockConfig {
        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void testGetUsersById() throws Exception {

        Mockito.when(userService.getUserById(1L))
                .thenReturn(Optional.of((new User("Oleg Koshurnikov", "koshuos@gmail.com", LocalDate.of(1982, 8, 4), 1L))));


        mockMvc.perform(get("/api/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Oleg Koshurnikov"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        Long userId = 1L;

        when(userService.getUserById(userId)).thenReturn(Optional.of(new User()));
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }
}
