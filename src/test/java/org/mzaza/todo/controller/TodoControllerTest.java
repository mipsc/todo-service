package org.mzaza.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mzaza.todo.dto.TodoDTO;
import org.mzaza.todo.dto.TodoTaskDTO;
import org.mzaza.todo.entity.TodoTask;
import org.mzaza.todo.exception.TodoNotFoundException;
import org.mzaza.todo.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    private List<TodoTask> tasks;
    private List<TodoTaskDTO> taskDTOS;

    @BeforeEach
    void setUp() {
        tasks = new ArrayList<>();
        tasks.add(new TodoTask().setTitle("title1").setDescription("description1"));
        tasks.add(new TodoTask().setTitle("title2").setDescription("description2"));
        tasks.add(new TodoTask().setTitle("title3").setDescription("description3"));
        tasks.add(new TodoTask().setTitle("title4").setDescription("description4"));
        taskDTOS = tasks.stream().map(t -> new TodoTaskDTO(t.getTitle(), t.getDescription())).toList();
    }

    @Test
    public void shouldReturnTodoById() throws Exception {
        TodoDTO todoDTO = new TodoDTO(1L, "Title", "Description", taskDTOS);
        when(todoService.getTodo(1L)).thenReturn(todoDTO);
        mockMvc.perform(get("/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.tasks").isArray())
                .andExpect(jsonPath("$.tasks[0].title").value("title1"))
                .andExpect(jsonPath("$.tasks[0].description").value("description1"));
    }

    @Test
    public void shouldThrowExceptionWhenIdNotFound() throws Exception {
        when(todoService.getTodo(10L)).thenThrow(new TodoNotFoundException("Todo doesn't exist"));
        mockMvc.perform(get("/todos/10"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Todo doesn't exist"));
    }

    @Test
    public void shouldReturnValidationErrorWhenTitleIsEmpty() throws Exception {
        TodoDTO todoDTO = new TodoDTO(1L, "", "Legit Description", taskDTOS);
        String todoStr = jacksonObjectMapper.writeValueAsString(todoDTO);
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoStr)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errors.title").value("Title must not be empty"));

    }
    @Test
    public void shouldReturnValidationErrorWhenDescriptionTooShort() throws Exception {
        TodoDTO todoDTO = new TodoDTO(1L, "Legit title", "short", taskDTOS);
        String todoStr = jacksonObjectMapper.writeValueAsString(todoDTO);
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoStr)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errors.description").value("Description cannot be shorter than 10 characters"));
    }
    @Test
    public void shouldReturnValidationErrorWhenATaskHasNoTitle() throws Exception {

        TodoDTO todoDTO = new TodoDTO(1L, "Legit title", "Description", List.of(new TodoTaskDTO("", "Description")));
        String todoStr = jacksonObjectMapper.writeValueAsString(todoDTO);
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoStr)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errors['tasks[0].title']").value("Task title must not be empty"));
    }
    @Test
    public void shouldReturnValidationErrorWhenATaskHasTooShortDescription() throws Exception {

        TodoDTO todoDTO = new TodoDTO(1L, "Legit title", "Description", List.of(new TodoTaskDTO("Legit task title", "short")));
        String todoStr = jacksonObjectMapper.writeValueAsString(todoDTO);
        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoStr)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errors['tasks[0].description']").value("Task description cannot be shorter than 10 characters"));
    }

}
