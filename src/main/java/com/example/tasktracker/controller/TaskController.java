package com.example.tasktracker.controller;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/task")
public class TaskController {
    Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping
    public Flux<ResponseEntity<?>> getAllTasks() {

        Flux<TaskResponse> allTasks = taskService.getAllTasks();


        allTasks.doOnNext(task -> logger.info(task.status()))
                .subscribe();

// Return a Flux<ResponseEntity<TaskResponse>>
        return allTasks.map(ResponseEntity::ok);

    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<TaskResponse> createTask(@RequestBody TaskRequest taskRequest) {
        Mono<TaskResponse> mono = taskService.save(taskRequest);
        logger.info(mono.toString());
        return mono;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<TaskResponse> editTask(@RequestBody TaskRequest taskRequest) {
        logger.info(taskRequest.toString());
        return taskService.updateTask(taskRequest);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<String>> deleteTask(@PathVariable String id) {
        return taskService.delete(id)
                .map(response -> ResponseEntity.ok("delete successful"));
    }

}
