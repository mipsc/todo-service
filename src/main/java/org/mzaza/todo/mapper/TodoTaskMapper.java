package org.mzaza.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mzaza.todo.dto.TodoTaskDTO;
import org.mzaza.todo.entity.TodoTask;

@Mapper(componentModel = "spring")
public interface TodoTaskMapper {

    TodoTaskDTO toDTO(TodoTask todoTask);

    @Mapping(target = "id", ignore = true)
    TodoTask toEntity(TodoTaskDTO todoTaskDTO);
}
