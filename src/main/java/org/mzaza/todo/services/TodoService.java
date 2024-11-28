package org.mzaza.todo.services;

import org.mzaza.todo.dto.TodoDTO;

import java.util.List;


public interface TodoService {

    TodoDTO getTodo(Long id);

    List<TodoDTO> getAllTodos();

    TodoDTO addTodo(TodoDTO todoDTO);

    TodoDTO updateTodo(Long id, TodoDTO todoDTO);

    void deleteTodo(Long id);
}
