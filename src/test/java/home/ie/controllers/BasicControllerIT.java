package home.ie.controllers;

import home.ie.TodoApplication;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.Charset;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class,
            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasicControllerIT {

    HttpHeaders headers = createHeaders("admin", "12345");

    @Autowired
    private TestRestTemplate template;
    @Test
    void welcome() {
//        ResponseEntity<String> response =
//                template.getForEntity("/welcome", String.class);

        ResponseEntity<String> response = template.exchange("/welcome",
                HttpMethod.GET,
                new HttpEntity<>(null, headers), String.class);

        assertEquals("Hello World!", response.getBody());
    }

    @Test
    void welcomeWithObject() throws JSONException {
//        ResponseEntity<String> response =
//                template.getForEntity("/welcome-with-object", String.class);

        String expected = "{\"message\":\"Hello World !\"}";
        ResponseEntity<String> response = template.exchange("/welcome-with-object",
                HttpMethod.GET,
                new HttpEntity<>(null, headers), String.class);
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    void welcomeWithParameter() throws JSONException {
//        ResponseEntity<String> response =
//                template.getForEntity("/welcome-with-parameter/name/Buddy", String.class);

//        String greeting = "Hello World, Octavian";
        String expected = "{\"message\":\"Hello World, Octavian!!!\"}";
        ResponseEntity<String> response = template.exchange("/welcome-with-parameter/name/Octavian",
                HttpMethod.GET,
                new HttpEntity<>(null, headers), String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.getEncoder().encode(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }
}