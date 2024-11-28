package org.mzaza.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mzaza.todo.dto.TodoDTO;
import org.mzaza.todo.entity.Todo;

@Mapper(componentModel = "spring", uses = {TodoTaskMapper.class})
public interface TodoMapper {

    @Mapping(target = "tasks", source = "todoTasks")
    TodoDTO toDTO(Todo todo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdated", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "todoTasks", source = "tasks")
    Todo toEntity(TodoDTO todoDTO);

}
