package org.mzaza.todo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "todo_task")
@Accessors(chain = true)
@Data
public class TodoTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "Title is mandatory")
    private String title;
    private String description;

}
