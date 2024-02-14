package com.example.tasktracker.mapper;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.entity.Status;
import com.example.tasktracker.entity.Task;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskMapperTest {

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Test
    void testTaskToTaskResponse() {

        LocalDateTime createdAt = LocalDateTime.of(2024, 2, 12, 10, 30);
        Task task = new Task();
        UUID id = UUID.randomUUID();
        task.setId(id);
        task.setCreatedAt(createdAt);


        TaskResponse taskResponse = taskMapper.taskToTaskResponse(task);

        assertNotNull(taskResponse);
        assertEquals(id.toString(), taskResponse.id());
        assertEquals("2024-02-12T10:30:00", taskResponse.createdAt()); // Assuming date format as string
    }

    @Test
    void testTaskRequestToTask() {

        TaskRequest taskRequest = new TaskRequest(UUID.randomUUID().toString(), "Sample Task", "description", Status.PROGRESS);
        Task task = taskMapper.taskRequestToTask(taskRequest);


        assertNotNull(task);
        assertNotNull(task.getCreatedAt());
        assertEquals("Sample Task", task.getName());
    }
}
