package home.ie.services;

import home.ie.controllers.Todo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {
    private static List<Todo> todos = new ArrayList<Todo>();

    private static int todoCount = 3;

    static {
        todos.add(new Todo(1, "Jack", "Learn Spring MVC", new Date(), false));
        todos.add(new Todo(2, "Jack", "Learn Struts", new Date(), false));
        todos.add(new Todo(3, "Jill", "Learn Hibernate", new Date(), false));
    }

    @Cacheable(cacheNames="todos", condition="#user.length < 10")
    public List<Todo> retrieveTodos(String user) {
        return todos.stream()
                .filter(todo -> todo.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public Todo addTodo(String name, String desc, Date targetDate, boolean isDone) {
        Todo newTodo = new Todo(todos.size()+1, name, desc, targetDate, isDone);

        todos.add(newTodo);

        return newTodo;
    }

    public Todo retrieveTodo(int id) {
        for (Todo todo: todos) {
            if (todo.getId() == id) {
                return todo;
            }
        }

        return null;
    }

    public Todo update(Todo todo) {
        Todo existingTodo = retrieveTodo(todo.getId());
        if (existingTodo != null) {
            existingTodo.setDesc(todo.getDesc());
            existingTodo.setTargetDate(todo.getTargetDate());
            existingTodo.setUser(todo.getUser());
            existingTodo.setDone(todo.isDone());
        }

        return existingTodo;
    }

    public Todo deleteById(int id) {
        Todo seekTodo = null;

        for(Todo todo: todos) {
            if (todo.getId() == id) {
                seekTodo = todo;
                break;
            }
        }

        if (seekTodo != null) {
            todos.remove(seekTodo);
        }

        return seekTodo;
    }
}
