package home.ie.controllers;

import home.ie.exceptions.TodoNotFoundException;
import home.ie.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/users/{name}/todos")
    public List<Todo> retrieveTodos(@PathVariable String name) {
        return todoService.retrieveTodos(name);
    }

    @GetMapping(path = "/users/{name}/todos/{id}")
    public Todo retrieveTodo(@PathVariable String name,
                             @PathVariable int id) {

        Todo todo = todoService.retrieveTodo(id);

        if (todo == null) {
            throw new TodoNotFoundException("Todo Not Found");
        }

        return todo;
    }

    @PostMapping("/users/{name}/todos")
    public ResponseEntity<?> addTodo( @PathVariable String name,
                           @Valid @RequestBody Todo todo) {

        Todo createdTodo = todoService.addTodo(name, todo.getDesc(),
                todo.getTargetDate(), todo.isDone());

        if (createdTodo == null) {
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()

                .path("/{id}").buildAndExpand(createdTodo.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @PutMapping("/users/{name}/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable String name, @PathVariable int id,
                                           @RequestBody Todo todo) {

        todoService.update(todo);

        return new ResponseEntity<Todo>(todo, HttpStatus.OK);
    }

    @DeleteMapping("/users/{name}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String name, @PathVariable int id) {

        Todo todo = todoService.deleteById(id);

        if (todo != null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/users/dummy-service")
    public Todo errorService() {

        throw new RuntimeException("Some Exception Occurred");

    }
}
