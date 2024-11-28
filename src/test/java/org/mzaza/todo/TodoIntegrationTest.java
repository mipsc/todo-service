package org.mzaza.todo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mzaza.todo.dto.TodoDTO;
import org.mzaza.todo.dto.TodoTaskDTO;
import org.mzaza.todo.entity.TodoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TodoIntegrationTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer("postgres:16")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeAll
    public static void setUp() {
        postgreSQLContainer.start();
    }
    @AfterAll
    public static void tearDown() {
        postgreSQLContainer.stop();
    }
    @Test
    public void shouldCreateAndGetTodo() {
        List<TodoTask> tasks = new ArrayList<>();
        tasks.add(new TodoTask().setTitle("title1").setDescription("description1"));
        tasks.add(new TodoTask().setTitle("title2").setDescription("description2"));
        tasks.add(new TodoTask().setTitle("title3").setDescription("description3"));
        tasks.add(new TodoTask().setTitle("title4").setDescription("description4"));
        List<TodoTaskDTO> taskDTOS = tasks.stream().map(t -> new TodoTaskDTO(t.getTitle(), t.getDescription())).toList();

        String url = "http://localhost:" + port + "/todos";

        TodoDTO todoDTO = new TodoDTO(null, "Integration title", "Integration description", taskDTOS);
        TodoDTO createdDTO = restTemplate.postForObject(url, todoDTO, TodoDTO.class);

        assertThat(createdDTO).isNotNull();
        assertThat(createdDTO.id()).isNotNull();
        assertThat(createdDTO.title()).isEqualTo(todoDTO.title());
        assertThat(createdDTO.description()).isEqualTo(todoDTO.description());
        assertThat(createdDTO.tasks()).isEqualTo(taskDTOS);

        TodoDTO fetchedTodoDTO = restTemplate.getForObject(url + "/" + createdDTO.id(), TodoDTO.class);
        assertThat(fetchedTodoDTO).isEqualTo(createdDTO);

    }
}
