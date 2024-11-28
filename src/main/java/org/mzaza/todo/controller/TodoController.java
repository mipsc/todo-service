package org.mzaza.todo.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.mzaza.todo.dto.TodoDTO;
import org.mzaza.todo.services.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@AllArgsConstructor
public class TodoController {

    private TodoService todoService;

    @GetMapping("/{id}")
    public TodoDTO getTodo(@PathVariable Long id) {
        return todoService.getTodo(id);
    }

    @GetMapping
    public List<TodoDTO> getAllTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDTO addTodo(@Valid @RequestBody TodoDTO todo) {
        return todoService.addTodo(todo);
    }

    @PutMapping("/{id}")
    public TodoDTO updateTodo(@PathVariable Long id, @Valid @RequestBody TodoDTO todo) {
        return todoService.updateTodo(id, todo);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }

}
