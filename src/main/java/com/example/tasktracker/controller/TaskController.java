package com.example.tasktracker.controller;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.entity.Task;
import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task")
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
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) {
        Task save = taskService.save(taskRequest);
        logger.info("task request : {}", save);
        return taskMapper.taskToTaskResponse(save);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse editTask(@RequestBody TaskRequest taskRequest) {
        logger.info("task edit controller {} ", taskRequest);
        return taskMapper.taskToTaskResponse(taskService.updateTask(taskRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        taskService.delete(id);
        logger.info("task deleted with id : {}", id);
        return ResponseEntity.ok("task delete succesfull");
    }

}
