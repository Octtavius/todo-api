package home.ie.controllers;

import home.ie.services.TodoService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
class TodoControllerTest {

    private static final int CREATED_TODO_ID = 2;
    private static final int UPDATED_TODO_ID = 1;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoService service;

    @Test
    void retrieveTodos() throws Exception {
        //GIVEN
        List< Todo > mockList = Arrays.asList(
                new Todo(1, "Jack", "Learn Spring MVC", new Date(), false),
                new Todo(2, "Jack", "Learn Struts", new Date(), false)
        );

        when(service.retrieveTodos(anyString())).thenReturn(mockList);

        //WHEN
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/users/Jack/todos")
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        //THEN
        String expected = "[" + "{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}" + "," +
                "{id:2,user:Jack,desc:\"Learn Struts\",done:false}" + "]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    void retrieveTodo() throws Exception {
        Todo mockTodo = new Todo(1, "Jack", "Learn Spring MVC", new Date(), false);

        when(service.retrieveTodo(anyInt())).thenReturn(mockTodo);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/users/Jack/todos/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        String expect = "{id:1,user:Jack,desc:\"Learn Spring MVC\",done:false}";

        JSONAssert.assertEquals(expect,
                result.getResponse().getContentAsString(), false);
    }

    @Test
    void testAddTodo() throws Exception {
        Todo mockTodo = new Todo(CREATED_TODO_ID, "Jack", "Learn Spring MVC", new Date(), false);
        String todo = "{\"user\":\"Jack\",\"desc\":\"Learn Spring MVC\",\"done\":\"false\"}";
        when(service.addTodo(anyString(), anyString(), isNull(), anyBoolean()))
                .thenReturn(mockTodo);
        mvc.perform(
                MockMvcRequestBuilders.post("/users/Jack/todos")
                        .content(todo)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("location",
                        containsString("/users/Jack/todos/" + CREATED_TODO_ID)));


    }

    @Test
    public void testAddTodo_withValidationError() throws Exception {
        Todo mockTodo = new Todo(CREATED_TODO_ID, "Jack",
                "Learn Spring MVC", new Date(), false);
        String todo = "{\"user\":\"Jack\",\"desc\":\"Learn\",\"done\":\"false\"}";
        when(service.addTodo(anyString(), anyString(), isNull(), anyBoolean()))
                .thenReturn(mockTodo);
        MvcResult result = mvc
                .perform(
                        MockMvcRequestBuilders.post("/users/Jack/todos")
                                .content(todo)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void updateTodo() throws Exception {
        Todo mockTodo = new Todo(UPDATED_TODO_ID, "Jack", "Learn Spring MVC 5",
                new Date(), false);

        String todo = "{\"user\":\"Jack\",\"desc\":\"Learn Spring MVC 5\",\"done\":false}";

        when(service.update(mockTodo)).thenReturn(mockTodo);
        MvcResult result = mvc.perform(MockMvcRequestBuilders.put("/users/Jack/todos/" + UPDATED_TODO_ID)
                    .content(todo)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JSONAssert.assertEquals(todo, result.getResponse().getContentAsString(), false);
    }

    @Test
    void deleteTodo() throws Exception {
        Todo mockTodo = new Todo(1, "Jack", "Learn Spring MVC", new Date(), false);

        when(service.deleteById(anyInt())).thenReturn(mockTodo);

        mvc.perform(MockMvcRequestBuilders.delete("/users/Jack/todos/" + mockTodo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void errorService() {
    }
}