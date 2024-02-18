package com.example.tasktracker.controller;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/task")
//@Tag(name = "TaskController")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    Logger logger = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }


    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        logger.info("all tasks controller");
        return ResponseEntity.ok(taskService.getAllTasks().stream()
                .map(taskMapper::taskToTaskResponse)
                .toList());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create task", description = "Create task")
    @SecurityRequirement(name = "Bearer Authentication")
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) throws IOException {
        Task save = taskService.save(taskRequest);
        logger.info("task request : {}", save);
        return taskMapper.taskToTaskResponse(save);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Edit task", description = "Edit task")
    @SecurityRequirement(name = "Bearer Authentication")
    public TaskResponse editTask(@RequestBody TaskRequest taskRequest) {
        logger.info("task edit controller {} ", taskRequest);
        return taskMapper.taskToTaskResponse(taskService.updateTask(taskRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Delete task")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        taskService.delete(id);
        logger.info("task deleted with id : {}", id);
        return ResponseEntity.ok("task delete succesfull");
    }

}
