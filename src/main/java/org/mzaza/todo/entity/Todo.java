package org.mzaza.todo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "todo")
@Accessors(chain = true)
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "Title is mandatory")
    private String title;
    @Column
    private String description;
    // These two fields are hypothetically required by
    // the system and not meant to be presented to the end-user.
    @Column
    private LocalDateTime creationDate;
    @Column
    private LocalDateTime lastUpdated;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<TodoTask> todoTasks;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}