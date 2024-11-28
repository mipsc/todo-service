package org.mzaza.todo.services;

import lombok.AllArgsConstructor;
import org.mzaza.todo.dto.TodoDTO;
import org.mzaza.todo.entity.Todo;
import org.mzaza.todo.exception.TodoNotFoundException;
import org.mzaza.todo.mapper.TodoMapper;
import org.mzaza.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {
    TodoMapper todoMapper;
    TodoRepository todoRepository;
    public TodoDTO getTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo with id: " + id + " is not found"));
        return todoMapper.toDTO(todo);
    }

    public List<TodoDTO> getAllTodos() {
        return todoRepository.findAll()
                .stream()
                .map(todoMapper::toDTO)
                .toList();
    }

    public TodoDTO addTodo(TodoDTO todoDTO) {
        Todo todo = todoMapper.toEntity(todoDTO);
        var savedTodo = todoRepository.save(todo);
        return todoMapper.toDTO(savedTodo);
    }

    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        Todo foundTodo = todoRepository.findById(id).orElseThrow(() -> new TodoNotFoundException(""));
        foundTodo
                .setTitle(todoDTO.title())
                .setDescription(todoDTO.description());
        Todo updatedTodo = todoRepository.save(foundTodo);
        return todoMapper.toDTO(updatedTodo);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

}
