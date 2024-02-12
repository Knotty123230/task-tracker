package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITaskService {
    Mono<TaskResponse> save(TaskRequest taskRequest);

    Flux<TaskResponse> getAllTasks();

    Mono<TaskResponse> updateTask(TaskRequest taskRequest);

    Mono<Task> delete(String id);
}
