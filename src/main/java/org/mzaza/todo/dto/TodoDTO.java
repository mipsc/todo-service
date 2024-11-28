package org.mzaza.todo.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TodoDTO(
        Long id,
        @NotBlank(message = "Title must not be empty")
        @Size(message = "Title cannot exceed 100 characters", max = 100)
        String title,
        @Size(message = "Description cannot be shorter than 10 characters", min = 10) String description,
        @Valid List<TodoTaskDTO> tasks
) {
}
