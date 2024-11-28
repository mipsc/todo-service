package org.mzaza.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoTaskDTO(
        @NotBlank(message = "Task title must not be empty")
        @Size(message = "Task title cannot exceed 100 characters", max = 100)
        String title,
        @Size(message = "Task description cannot be shorter than 10 characters", min = 10) String description
) {
}
