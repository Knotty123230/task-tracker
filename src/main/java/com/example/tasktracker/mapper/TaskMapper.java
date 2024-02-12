package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.entity.Status;
import com.example.tasktracker.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    TaskResponse taskToTaskResponse(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    Task taskRequestToTask(TaskRequest taskRequest);

    Status statusFromString(String status);
}
