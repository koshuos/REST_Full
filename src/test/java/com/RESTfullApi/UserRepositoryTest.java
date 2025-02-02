package com.RESTfullApi;

import com.RESTfullApi.Entity.User;
import com.RESTfullApi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser() {
        User user = new User("Oleg", "koshuos@gmail.com", LocalDate.of(1982, 8, 4), 1L);

        User save = userRepository.save(user);

        assertNotNull (save.getId());
        assertEquals ("Oleg", save.getName());

    }
}
