package com.RESTfullApi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void testCreateUser() throws Exception {
		String userJson = """
            {
                "name": "Oleg",
                "email": "koshuos@gmail.com",
                "birthDate": "2000-02-06T23:00:00.000Z"
            }
        """;
		mockMvc.perform(post("/api")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Oleg"))
				.andExpect(jsonPath("$.email").value("koshuos@gmail.com"));
	}

}
