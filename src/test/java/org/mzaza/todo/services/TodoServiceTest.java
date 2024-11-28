package org.mzaza.todo.services;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mzaza.todo.dto.TodoDTO;
import org.mzaza.todo.dto.TodoTaskDTO;
import org.mzaza.todo.entity.Todo;
import org.mzaza.todo.entity.TodoTask;
import org.mzaza.todo.exception.TodoNotFoundException;
import org.mzaza.todo.mapper.TodoMapper;
import org.mzaza.todo.repository.TodoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    TodoRepository todoRepository;
    @Mock
    TodoMapper todoMapper;
    @InjectMocks
    TodoServiceImpl todoService;

    @Test
    public void shouldReturnFoundTodo() {
        List<TodoTask> tasks = new ArrayList<>();
        tasks.add(new TodoTask().setTitle("title1").setDescription("description1"));
        tasks.add(new TodoTask().setTitle("title2").setDescription("description2"));
        tasks.add(new TodoTask().setTitle("title3").setDescription("description3"));
        tasks.add(new TodoTask().setTitle("title4").setDescription("description4"));
        Todo todo = new Todo()
                .setId(1L)
                .setTitle("Title")
                .setDescription("Description")
                .setTodoTasks(tasks);
        List<TodoTaskDTO> taskDTOS = tasks.stream().map(t -> new TodoTaskDTO(t.getTitle(), t.getDescription())).toList();
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoMapper.toDTO(todo)).thenReturn(new TodoDTO(1L, "Title", "Description", taskDTOS));

        TodoDTO todoDTO = todoService.getTodo(1L);

        assertNotNull(todoDTO);
        assertEquals(todoDTO.id(), 1L);
        assertEquals(todoDTO.title(), "Title");
        assertEquals(todoDTO.description(), "Description");
        assertThat(taskDTOS, is(todoDTO.tasks()));
    }

    @Test
    public void shouldThrowExceptionWhenTodoNotFound() {
        when(todoRepository.findById(10L)).thenReturn(Optional.empty());
        Exception e = assertThrows(TodoNotFoundException.class, () -> todoService.getTodo(10L));
        assertEquals(e.getMessage(), "Todo with id: " + 10 + " is not found");
    }
}
