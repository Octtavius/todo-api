package home.ie.controllers;

import home.ie.TodoApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class,
            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicControllerIT {

    @Autowired
    private TestRestTemplate template;
    @Test
    void welcome() {
        ResponseEntity<String> response =
                template.getForEntity("/welcome", String.class);

        assertEquals("Hello World!", response.getBody());
    }

    @Test
    void welcomeWithObject() {
        ResponseEntity<String> response =
                template.getForEntity("/welcome-with-object", String.class);

        assertTrue(response.getBody().contains("Hello World !"));
    }

    @Test
    void welcomeWithParameter() {
        ResponseEntity<String> response =
                template.getForEntity("/welcome-with-parameter/name/Buddy", String.class);

        assertTrue(response.getBody().contains("Hello World, Buddy"));
    }
}