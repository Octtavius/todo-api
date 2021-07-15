package home.ie.controllers;

import home.ie.TodoApplication;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerIT {

    HttpHeaders headers = createHeaders("admin", "12345");
    @Autowired
    private TestRestTemplate template;

    @Test
    public void retrieveTodos() throws Exception {
        String expected = "[" + "{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}" + "," +
                "{id:2,user:Jack,desc:\"Learn Struts\",done:false}" + "]";

        ResponseEntity< String > response = template.exchange(
                "/users/Jack/todos",
                HttpMethod.GET,
                new HttpEntity< String >(null, headers), String.class);
        JSONAssert.assertEquals(expected,
                response.getBody(), false);
    }

    @Test
    public void addTodo() throws Exception {
        Todo todo = new Todo(-1, "Jill", "Learn Hibernate",
                new Date(), false);
        URI location = template
                .postForLocation("/users/Jill/todos", todo);
        assertThat(location.getPath(),
                containsString("/users/Jill/todos/4"));
    }

    @Test
    public void updatedTodo() throws Exception {
        String expected = "{id:3,user:Jill,desc:\"Learn Spring MVC 5\",done:false}";
        Todo todo = new Todo(3, "Jill", "Learn Spring MVC 5", new Date(), false);

        ResponseEntity<String> response = template.exchange(
                "/users/Jill/todos/" + todo.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(todo, headers), String.class);

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void deleteTodo() throws Exception {
        ResponseEntity<String> response = template.exchange(
                "/users/Jill/todos/4",
                HttpMethod.DELETE,
                new HttpEntity<>(null, headers), String.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
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
